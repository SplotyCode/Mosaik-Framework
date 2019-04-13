package io.github.splotycode.mosaik.networking.config;

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
import io.github.splotycode.mosaik.networking.service.Service;
import io.github.splotycode.mosaik.networking.service.ServiceStatus;
import io.netty.channel.Channel;

import java.io.File;

public class ConfigService extends StaticConfigProvider implements Service {

    public static final PacketRegistry<SerializedPacket> PACKET_REGISTRY = new PacketRegistry<>();

    static {
        PACKET_REGISTRY.register(ConfigRequestUpdate.class);
        PACKET_REGISTRY.register(ConfigNoUpdate.class);
        PACKET_REGISTRY.register(ConfigUpdate.class);
        PACKET_REGISTRY.register(KAUpdate.class);
    }

    private boolean keepAlive;
    private int port;
    private String serverAddress;

    public ConfigService(File file, boolean keepAlive, int port, String serverAddress) {
        super(file);
        this.keepAlive = keepAlive;
        this.port = port;
        this.serverAddress = serverAddress;
    }

    private TCPServer server;
    private TCPClient client;

    @Override
    public void start() {
        if (serverAddress == null) {
            server = TCPServer.create()
                    .port(port)
                    .usePacketSystem(DefaultPacketSystem.createSerialized(PACKET_REGISTRY))
                    .handler("packetHandler", new AnnotationContentHandler(new ConfigServerHandler(this, keepAlive)))
                    .bind();
        } else {
            client = TCPClient.create()
                    .port(port)
                    .usePacketSystem(DefaultPacketSystem.createSerialized(PACKET_REGISTRY))
                    .handler("packetHandler", new AnnotationContentHandler(new ConfigClientHandler(this, keepAlive)))
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
}
