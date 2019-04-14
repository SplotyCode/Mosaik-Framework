package io.github.splotycode.mosaik.networking.master;

import io.github.splotycode.mosaik.networking.component.tcp.TCPClient;
import io.github.splotycode.mosaik.networking.component.tcp.TCPServer;
import io.github.splotycode.mosaik.networking.host.Host;
import io.github.splotycode.mosaik.networking.master.packets.DestroyPacket;
import io.github.splotycode.mosaik.networking.packet.PacketRegistry;
import io.github.splotycode.mosaik.networking.packet.handle.AnnotationContentHandler;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import io.github.splotycode.mosaik.networking.packet.system.DefaultPacketSystem;
import io.github.splotycode.mosaik.networking.service.Service;
import io.github.splotycode.mosaik.networking.service.ServiceStatus;
import io.github.splotycode.mosaik.networking.util.IpResolver;
import io.github.splotycode.mosaik.util.logger.Logger;
import io.github.splotycode.mosaik.util.task.TaskExecutor;
import io.github.splotycode.mosaik.util.task.types.RepeatableTask;
import io.github.splotycode.mosaik.valuetransformer.TransformerManager;
import io.netty.handler.ipfilter.IpFilterRule;
import io.netty.handler.ipfilter.IpFilterRuleType;
import io.netty.handler.ipfilter.RuleBasedIpFilter;
import lombok.Getter;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.TreeMap;

public class MasterService extends RepeatableTask implements Service {

    private final Logger logger = Logger.getInstance(getClass());

    private PacketRegistry<SerializedPacket> masterRegistry = new PacketRegistry<>();

    {
        masterRegistry.register(DestroyPacket.class);
    }

    private TreeMap<String, Host> roots;
    private String currentBest;
    private boolean self;
    private IpResolver ipResolver;

    private int port;

    private final TaskExecutor taskExecutor;
    private long taskID;

    @Getter private TCPServer server;
    @Getter private TCPClient client;

    @Getter private ServiceStatus status = ServiceStatus.UNKNOWN;

    public MasterService(long delay, TaskExecutor taskExecutor, int port, IpResolver ipResolver, TreeMap<String, Host> roots) {
        super("Master Sync", delay);
        this.taskExecutor = taskExecutor;
        this.roots = roots;
        this.ipResolver = ipResolver;
        this.port = port;
    }

    @Override
    public void run() {
        String best = getBestRoot();
        if (!best.equals(currentBest)) {
            logger.info("Best Primary Master switched from " +  currentBest + " to " + best);
            if (currentBest.equals(ipResolver.getIpAddress())) {
                server.shutdown();
            }
            if (best.equals(ipResolver.getIpAddress())) {
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

            for (Map.Entry<String, Host> root : roots.entrySet()) {
                if (root.getKey().hashCode() > currentBest.hashCode() && root.getValue().healthCheck().isOnline()) {
                    logger.info("Sending Destroy packet to " + root.getKey());
                    destroyMaster(root.getKey(), currentBest);
                }
            }
        } else {
            logger.info("Sync has not changed in the last " + (delay / 1000) + " seconds");
        }
    }

    private String getBestRoot() {
        for (Map.Entry<String, Host> root : roots.entrySet()) {
            if (root.getValue().healthCheck().isOnline()) {
                return root.getKey();
            }
        }
        return ipResolver.getIpAddress();
    }

    protected TCPServer createServer() {
        return TCPServer.create()
                .port(port).setDisplayName("Master")
                .usePacketSystem(DefaultPacketSystem.createSerialized(masterRegistry))
                .handler("ipFiler", new RuleBasedIpFilter(new IpFilterRule() {
                    @Override
                    public boolean matches(InetSocketAddress address) {
                        return roots.keySet().contains(TransformerManager.getInstance().transform(address, String.class));
                    }

                    @Override
                    public IpFilterRuleType ruleType() {
                        return IpFilterRuleType.ACCEPT;
                    }
                }))
                .handler("packetHandler", new AnnotationContentHandler(new MasterServerHandler()))
                .bind(true);
    }

    protected TCPClient createClient(String host) {
        return TCPClient.create()
                .host(host).port(port).setDisplayName("Master")
                .usePacketSystem(DefaultPacketSystem.createSerialized(masterRegistry))
                .bind(true);
    }

    protected void destroyMaster(String original, String better) {
        TCPClient.create()
                .host(original).setDisplayName("Master Destroy")
                .port(port)
                .usePacketSystem(DefaultPacketSystem.createSerialized(masterRegistry))
                .onBound((component, future) -> future.channel().writeAndFlush(new DestroyPacket(better)))
                .bind();
    }

    @Override
    public void start() {
        currentBest = getBestRoot();
        self = currentBest.equals(ipResolver.getIpAddress());
        logger.info("Best Root is " + currentBest + " Self: " + self);
        if (self) {
            server = createServer();
        } else {
            client = createClient(currentBest);
        }
        taskID = taskExecutor.runTask(this);
        status = ServiceStatus.RUNNING;
    }

    @Override
    public String displayName() {
        return "Master Sync";
    }

    @Override
    public void stop() {
        taskExecutor.stopTask(taskID);
        status = ServiceStatus.STOPPED;
    }

    @Override
    public String statusMessage() {
        if (status == null) return null;
        return self ? "Primary Master" : "Bound to " + currentBest;
    }
}
