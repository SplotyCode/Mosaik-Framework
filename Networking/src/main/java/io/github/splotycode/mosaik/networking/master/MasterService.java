package io.github.splotycode.mosaik.networking.master;

import io.github.splotycode.mosaik.networking.cloudkit.CloudKit;
import io.github.splotycode.mosaik.networking.component.NetworkComponent;
import io.github.splotycode.mosaik.networking.component.tcp.TCPClient;
import io.github.splotycode.mosaik.networking.component.tcp.TCPServer;
import io.github.splotycode.mosaik.networking.config.ConfigKey;
import io.github.splotycode.mosaik.networking.host.Host;
import io.github.splotycode.mosaik.networking.master.packets.DestroyPacket;
import io.github.splotycode.mosaik.networking.packet.PacketRegistry;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import io.github.splotycode.mosaik.networking.packet.system.DefaultPacketSystem;
import io.github.splotycode.mosaik.networking.service.ServiceStatus;
import io.github.splotycode.mosaik.networking.service.SingleComponentService;
import io.github.splotycode.mosaik.networking.util.MosaikAddress;
import io.github.splotycode.mosaik.util.logger.Logger;
import io.github.splotycode.mosaik.util.task.types.RepeatableTask;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.ipfilter.IpFilterRuleType;
import io.netty.handler.ipfilter.IpSubnetFilterRule;
import io.netty.handler.ipfilter.RuleBasedIpFilter;
import lombok.Getter;

import java.net.InetSocketAddress;
import java.util.Map;

@Getter
public class MasterService extends RepeatableTask implements SingleComponentService {

    public static final ConfigKey<Long> DAEMON_STATS_DELAY = new ConfigKey<>("master.daemon_upload_stats", long.class, 15 * 1000L);

    public static final ConfigKey<Long> MASTER_UPDATE_DELAY = new ConfigKey<>("master.update_delay", long.class,  8 * 1000L);
    public static final ConfigKey<Integer> PORT = new ConfigKey<>("master.port", int.class, -1);

    private final Logger logger = Logger.getInstance(getClass());

    private PacketRegistry<SerializedPacket> masterRegistry = new PacketRegistry<>();

    {
        masterRegistry.register(DestroyPacket.class);
    }

    private MosaikAddress currentBest;
    private boolean self;

    private TCPServer server;
    private TCPClient client;

    private int port;

    private long taskID;

    private CloudKit kit;


    private ServiceStatus status = ServiceStatus.UNKNOWN;

    public MasterService(CloudKit kit) {
        this(kit, kit.getConfig(MASTER_UPDATE_DELAY), kit.getConfig(PORT));
    }

    public MasterService(CloudKit kit, long delay, int port) {
        super("Master Sync", delay);
        this.port = port;
        this.kit = kit;
    }

    @Override
    public void run() {
        MosaikAddress best = getBestRoot();
        if (!best.equals(currentBest)) {
            logger.info("Best Primary Master switched from " +  currentBest + " to " + best);
            if (currentBest.equals(kit.getLocalIpResolver().getIpAddress())) {
                server.shutdown();
            }
            if (best.equals(kit.getLocalIpResolver().getIpAddress())) {
                logger.info("Optimal Primary master is this machine");
                if (client != null) {
                    client.shutdown();
                    client = null;
                }
                server = createServer();
            } else {
                client = createClient(best);
            }

            currentBest = best;

            for (Map.Entry<MosaikAddress, Host> root : kit.hostMap().entrySet()) {
                if (root.getKey().hashCode() > currentBest.hashCode() && root.getValue().healthCheck().isOnline()) {
                    logger.info("Sending Destroy packet to " + root.getKey());
                    destroyMaster(root.getKey());
                }
            }
        } else {
            logger.info("Sync has not changed in the last " + (delay / 1000) + " seconds");
        }
    }

    private MosaikAddress getBestRoot() {
        for (Map.Entry<MosaikAddress, Host> root : kit.hostMap().entrySet()) {
            if (root.getValue().healthCheck().isOnline()) {
                return root.getKey();
            }
        }
        return kit.getLocalIpResolver().getIpAddress();
    }

    protected TCPServer createServer() {
        return TCPServer.create()
                .port(port).setDisplayName("Master")
                .logging()
                .usePacketSystem(2, DefaultPacketSystem.createSerialized(masterRegistry))
                .childHandler(1, "ipFiler", new RuleBasedIpFilter(new IpSubnetFilterRule("10.10.1.1",16, IpFilterRuleType.REJECT)))
                .childHandler(5, "packetHandler", new MasterServerHandler(this))
                .bind(true);
    }

    protected TCPClient createClient(MosaikAddress host) {
        return TCPClient.create()
                .host(host).port(port).setDisplayName("Master")
                .usePacketSystem(2, DefaultPacketSystem.createSerialized(masterRegistry))
                .handler(5, "packetHandler", new MasterClientHandler(kit))
                .bind(true);
    }

    protected void destroyMaster(MosaikAddress original) {
        TCPClient.create()
                .host(original).setDisplayName("Master Destroy")
                .port(port)
                .usePacketSystem(2, DefaultPacketSystem.createSerialized(masterRegistry))
                .onBound((component, future) -> future.channel().writeAndFlush(new DestroyPacket(currentBest.asString())))
                .bind();
    }

    @Override
    public void start() {
        currentBest = getBestRoot();
        self = currentBest.equals(kit.getLocalIpResolver().getIpAddress());
        logger.info("Best Root is " + currentBest + " Self: " + self);
        if (self) {
            server = createServer();
        } else {
            client = createClient(currentBest);
        }
        taskID = kit.getLocalTaskExecutor().runTask(this);
        status = ServiceStatus.RUNNING;
    }

    @Override
    public String displayName() {
        return "Master Sync";
    }

    @Override
    public void stop() {
        kit.getLocalTaskExecutor().stopTask(taskID);
        status = ServiceStatus.STOPPED;
    }

    @Override
    public String statusMessage() {
        if (status == null) return null;
        return self ? "Primary Master" : "Bound to " + currentBest;
    }

    public Host getHostByCtx(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        Host host = kit.hostMap().get(MosaikAddress.from((InetSocketAddress) channel.remoteAddress()));
        if (host instanceof RemoteMasterHost) {
            ((RemoteMasterHost) host).setChannel(channel);
        }
        return host;
    }

    @Override
    public CloudKit kit() {
        return kit;
    }

    @Override
    public NetworkComponent component() {
        return self ? server : client;
    }
}
