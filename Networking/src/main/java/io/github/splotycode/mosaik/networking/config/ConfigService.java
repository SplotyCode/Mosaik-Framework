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
import io.github.splotycode.mosaik.util.listener.StringListenerHandler;
import io.netty.channel.Channel;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.Map;

public class ConfigService implements Service, ConfigProvider {

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

    public ConfigService(boolean keepAlive, int port, String serverAddress) {
        this.keepAlive = keepAlive;
        this.port = port;
        this.serverAddress = serverAddress;
    }

    private Yaml yaml = new Yaml();
    private Map<String, ConfigEntry> config = new HashMap<>();

    private TCPServer server;
    private TCPClient client;

    private StringListenerHandler<ConfigChangeListener> handler = new StringListenerHandler<>();

    @Override
    public void set(String key, Object value) {
        ConfigEntry entry = getEntry(key);
        entry.setValue(value);
        handler.call(key, listener -> listener.onChange(key, entry));
    }

    @Override
    public ConfigEntry getEntry(String key) {
        return config.get(key);
    }

    @Override
    public Iterable<ConfigEntry> getEntries() {
        return config.values();
    }

    @Override
    public Iterable<Map.Entry<String, Object>> getRawEntries() {
        HashMap<String, Object> config = new HashMap<>(this.config.size(), 1);
        for (Map.Entry<String, ConfigEntry> value : this.config.entrySet()) {
            config.put(value.getKey(), value.getValue().getValue());
        }
        return config.entrySet();
    }

    @Override
    public StringListenerHandler<ConfigChangeListener> handler() {
        return handler;
    }

    @Override
    public String getRawConfig() {
        return yaml.dump(getRawEntries());
    }

    @Override
    public void setRawConfig(String rawConfig) {
        HashMap<String, ConfigEntry> config = new HashMap<>();
        for (Map.Entry<String, Object> value : ((Map<String, Object>) yaml.load(rawConfig)).entrySet()) {
            ConfigEntry entry = new ConfigEntry(value.getKey(), value.getValue());
            handler.call(value.getKey(), listener -> listener.onChange(value.getKey(), entry));
            config.put(value.getKey(), entry);
        }
        this.config = config;
    }

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
