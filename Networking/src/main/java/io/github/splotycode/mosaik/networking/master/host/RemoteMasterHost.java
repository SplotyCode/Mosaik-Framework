package io.github.splotycode.mosaik.networking.master.host;

import io.github.splotycode.mosaik.networking.cloudkit.CloudKit;
import io.github.splotycode.mosaik.networking.cloudkit.HostProvider;
import io.github.splotycode.mosaik.networking.config.ConfigKey;
import io.github.splotycode.mosaik.networking.healthcheck.HealthCheck;
import io.github.splotycode.mosaik.networking.host.AddressChangeListener;
import io.github.splotycode.mosaik.networking.master.MasterService;
import io.github.splotycode.mosaik.networking.master.packets.StartInstancePacket;
import io.github.splotycode.mosaik.networking.master.packets.StopInstancePacket;
import io.github.splotycode.mosaik.networking.service.Service;
import io.github.splotycode.mosaik.networking.statistics.HostStatistics;
import io.github.splotycode.mosaik.networking.util.MosaikAddress;
import io.github.splotycode.mosaik.util.listener.ListenerHandler;
import io.github.splotycode.mosaik.util.listener.MultipleListenerHandler;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

public class RemoteMasterHost implements MasterHost {

    public static final HostProvider PROVIDER = RemoteMasterHost::new;

    public static final ConfigKey<Long> HEALTH_THRESHOLD = new ConfigKey<>("master.host.health_threshold", long.class, 8 * 1000L);

    @Getter private long lastUpdate;
    private MasterHealthCheck healthCheck = new MasterHealthCheck();
    @Getter private HostStatistics statistics;

    @Getter private CloudKit cloudKit;
    private MosaikAddress address;

    @Setter @Getter private Channel channel;

    public RemoteMasterHost(CloudKit cloudKit, String address) {
        this.cloudKit = cloudKit;
        changeAddress(address);
    }

    public void changeAddress(String rawAddress) {
        MosaikAddress address = new MosaikAddress(rawAddress);
        handler.call(AddressChangeListener.class, (Consumer<AddressChangeListener>) listener -> listener.onChange(this.address, address));
        this.address = address;
    }

    private MultipleListenerHandler handler = new MultipleListenerHandler();

    @Override
    public String toString() {
        return "External-" + address().asString();
    }

    @Override
    public void update(HostStatistics statistics) {
       this.statistics = statistics;
       lastUpdate = System.currentTimeMillis();
    }

    @Override
    public void startNewInstance(String service) {
        channel.writeAndFlush(new StartInstancePacket(service));
    }

    @Override
    public void startNewInstance(Service service) {
        startNewInstance(service.displayName());
    }

    @Override
    public void stopService(String service, int port) {
        channel.writeAndFlush(new StopInstancePacket(service, port));
    }

    private class MasterHealthCheck implements HealthCheck {

        @Override
        public boolean isOnline() {
            long delay = System.currentTimeMillis() - lastUpdate;
            return delay <= cloudKit.getConfig(HEALTH_THRESHOLD) + cloudKit.getConfig(MasterService.DAEMON_STATS_DELAY);
        }
    }

    @Override
    public HealthCheck healthCheck() {
        return healthCheck;
    }

    @Override
    public MosaikAddress address() {
        return address;
    }

    @Override
    public ListenerHandler handler() {
        return null;
    }
}
