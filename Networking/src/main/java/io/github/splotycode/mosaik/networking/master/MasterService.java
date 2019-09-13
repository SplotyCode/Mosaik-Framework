package io.github.splotycode.mosaik.networking.master;

import io.github.splotycode.mosaik.networking.cloudkit.CloudKit;
import io.github.splotycode.mosaik.networking.component.NetworkComponent;
import io.github.splotycode.mosaik.networking.component.listener.standart.ListStatusListener;
import io.github.splotycode.mosaik.networking.component.tcp.TCPClient;
import io.github.splotycode.mosaik.networking.component.tcp.TCPServer;
import io.github.splotycode.mosaik.networking.config.ConfigKey;
import io.github.splotycode.mosaik.networking.host.Host;
import io.github.splotycode.mosaik.networking.master.host.RemoteMasterHost;
import io.github.splotycode.mosaik.networking.master.packets.DestroyPacket;
import io.github.splotycode.mosaik.networking.master.packets.DistributePacket;
import io.github.splotycode.mosaik.networking.packet.PacketRegistry;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import io.github.splotycode.mosaik.networking.packet.system.DefaultPacketSystem;
import io.github.splotycode.mosaik.networking.service.ServiceStatus;
import io.github.splotycode.mosaik.networking.statistics.CloudStatistics;
import io.github.splotycode.mosaik.networking.statistics.SingleComponentService;
import io.github.splotycode.mosaik.networking.util.MosaikAddress;
import io.github.splotycode.mosaik.util.ExceptionUtil;
import io.github.splotycode.mosaik.util.listener.Listener;
import io.github.splotycode.mosaik.util.logger.Logger;
import io.github.splotycode.mosaik.util.task.types.RepeatableTask;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Map;

@Getter
public class MasterService extends RepeatableTask implements SingleComponentService {

    public static final ConfigKey<Long> DAEMON_STATS_DELAY = new ConfigKey<>("master.daemon_upload_stats", long.class, 15 * 1000L);

    public static final ConfigKey<Long> MASTER_UPDATE_DELAY = new ConfigKey<>("master.update_delay", long.class,  8 * 1000L);
    public static final ConfigKey<Integer> PORT = new ConfigKey<>("master.port", int.class, -1);

    private final Logger logger = Logger.getInstance(getClass());

    private PacketRegistry<SerializedPacket> masterRegistry = new PacketRegistry<>(SerializedPacket.class);

    {
        masterRegistry.registerPackage(DestroyPacket.class);
    }

    private MosaikAddress currentBest;
    private boolean self;

    private TCPServer server;
    private TCPClient client;

    private ArrayList<Listener> clientListener = new ArrayList<>();
    private MasterClientHandler clientHandler;
    private MasterServerHandler serverHandler = new MasterServerHandler(this);

    private int port;

    private long taskID;

    private CloudKit kit;
    private CloudStatistics statistics;

    private ServiceStatus status = ServiceStatus.UNKNOWN;

    public MasterService(CloudKit kit) {
        this(kit, kit.getConfig(MASTER_UPDATE_DELAY), kit.getConfig(PORT));
    }

    public MasterService(CloudKit kit, long delay, int port) {
        super("Master Sync", delay);
        this.port = port;
        this.kit = kit;
        clientHandler = new MasterClientHandler(kit);
        statistics = new CloudStatistics(kit);
    }

    @Override
    public void run() {
        MosaikAddress best = getBestRoot();
        if (!best.equals(currentBest)) {
            boolean own = best.isLocal(kit);

            logger.info("Best Primary Master switched from " +  currentBest + " to " + best);
            kit.getHandler().call(MasterChangeListener.class, listener -> listener.onChange(own, best, currentBest));

            if (own) {
                logger.info("Optimal Primary master is this machine");
                client.shutdown();
                server = createServer();
            } else {
                server.shutdown();
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
                .childHandler(1, "ipFiler", kit.getIpFilter())
                .childHandler(5, "packetHandler", serverHandler)
                .bind(true);
    }

    protected TCPClient createClient(MosaikAddress host) {
        return TCPClient.create()
                .host(host).port(port).setDisplayName("Master")
                .usePacketSystem(2, DefaultPacketSystem.createSerialized(masterRegistry))
                .handler(5, "packetHandler", clientHandler)
                .addListener(new ListStatusListener(clientListener))
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
        return getHostByChannel(ctx.channel());
    }

    public Host getHostByChannel(Channel channel) {
        Host host = kit.hostMap().get(MosaikAddress.from(channel.remoteAddress()));
        if (host instanceof RemoteMasterHost) {
            ((RemoteMasterHost) host).setChannel(channel);
        }
        return host;
    }

    @Override
    public CloudKit cloudKit() {
        return kit;
    }

    public void sendMaster(SerializedPacket packet) {
        if (self) {
            sendSelf(packet);
        } else {
            clientHandler.channel.writeAndFlush(packet);
        }
    }

    public void sendAll(SerializedPacket packet) {
        sendSelf(packet);
        clientHandler.channel.writeAndFlush(new DistributePacket(packet, this));
    }

    public void sendSelf(SerializedPacket packet) {
        //client.nettyFuture().channel().writeAndFlush(packet);
        try {
            clientHandler.channelRead0(null, packet);
        } catch (Exception e) {
            ExceptionUtil.throwRuntime(e);
        }
    }

    @Override
    public NetworkComponent component() {
        return self ? server : client;
    }

    public MasterService addServerHandler(Object handler) {
        serverHandler.register(handler);
        return this;
    }

    public MasterService addClientHandler(Object handler) {
        if (handler instanceof Listener) {
            clientListener.add((Listener) handler);
        }
        clientHandler.register(handler);
        return this;
    }

}
