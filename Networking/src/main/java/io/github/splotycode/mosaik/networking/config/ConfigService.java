package io.github.splotycode.mosaik.networking.config;

import io.github.splotycode.mosaik.networking.cloudkit.CloudKit;
import io.github.splotycode.mosaik.networking.component.NetworkComponent;
import io.github.splotycode.mosaik.networking.component.tcp.TCPClient;
import io.github.splotycode.mosaik.networking.component.tcp.TCPServer;
import io.github.splotycode.mosaik.networking.config.packets.ConfigNoUpdate;
import io.github.splotycode.mosaik.networking.config.packets.ConfigRequestUpdate;
import io.github.splotycode.mosaik.networking.config.packets.ConfigUpdate;
import io.github.splotycode.mosaik.networking.config.packets.KAUpdate;
import io.github.splotycode.mosaik.networking.packet.PacketRegistry;
import io.github.splotycode.mosaik.networking.packet.handle.AnnotationContentHandler;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import io.github.splotycode.mosaik.networking.packet.system.DefaultPacketSystem;
import io.github.splotycode.mosaik.networking.service.ServiceStatus;
import io.github.splotycode.mosaik.networking.statistics.SingleComponentService;
import io.github.splotycode.mosaik.util.listener.Listener;
import io.netty.channel.Channel;

import java.io.File;

public class ConfigService extends StaticConfigProvider implements SingleComponentService {

    public static final PacketRegistry<SerializedPacket> PACKET_REGISTRY = new PacketRegistry<>(SerializedPacket.class);

    static {
        PACKET_REGISTRY.registerPackage(ConfigUpdate.class);
    }

    private boolean keepAlive;
    private int port;
    private String serverAddress;

    private CloudKit kit;

    public ConfigService(File file, boolean keepAlive, int port, String serverAddress, CloudKit kit) {
        super(file);
        this.keepAlive = keepAlive;
        this.port = port;
        this.serverAddress = serverAddress;
        this.kit = kit;
    }

    @Override
    public CloudKit kit() {
        return kit;
    }

    private TCPServer server;
    private TCPClient client;

    @Override
    public void start() {
        if (serverAddress == null) {
            Listener handler = new ConfigServerHandler(this, keepAlive);
            server = TCPServer.create()
                    .port(port).setDisplayName("Config")
                    .usePacketSystem(2, DefaultPacketSystem.createSerialized(PACKET_REGISTRY))
                    .addListener(handler)
                    .childHandler(1, "ipFilter", kit.getIpFilter())
                    .childHandler(5, "packetHandler", new AnnotationContentHandler<>(SerializedPacket.class, handler))
                    .bind();
        } else {
            client = TCPClient.create()
                    .port(port).setDisplayName("Config")
                    .usePacketSystem(2, DefaultPacketSystem.createSerialized(PACKET_REGISTRY))
                    .handler(5, "packetHandler", new ConfigClientHandler(this, keepAlive))
                    .bind();
        }
    }

    @Override
    public void stop() {
        if (server != null) {
            server.shutdown();
        }
        if (client != null) {
            client.shutdown();
        }
    }

    @Override
    public ServiceStatus getStatus() {
        Channel channel = (serverAddress == null ? server : client).nettyFuture().channel();
        return channel.isOpen() ? ServiceStatus.RUNNING : ServiceStatus.STOPPED;
    }

    @Override
    public String statusMessage() {
        if (getStatus() != ServiceStatus.RUNNING) return null;
        return serverAddress == null ? "Server" : "Bound to " + serverAddress;
    }

    @Override
    public NetworkComponent component() {
        return serverAddress == null ? server : client;
    }
}
