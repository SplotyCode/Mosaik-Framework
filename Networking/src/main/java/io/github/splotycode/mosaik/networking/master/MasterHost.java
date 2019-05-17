package io.github.splotycode.mosaik.networking.master;

import io.github.splotycode.mosaik.networking.cloudkit.CloudKit;
import io.github.splotycode.mosaik.networking.cloudkit.HostProvider;
import io.github.splotycode.mosaik.networking.config.ConfigKey;
import io.github.splotycode.mosaik.networking.healthcheck.HealthCheck;
import io.github.splotycode.mosaik.networking.host.AddressChangeListener;
import io.github.splotycode.mosaik.networking.statistics.HostStatistics;
import io.github.splotycode.mosaik.networking.statistics.StatisticalHost;
import io.github.splotycode.mosaik.networking.util.MosaikAddress;
import io.github.splotycode.mosaik.util.listener.ListenerHandler;
import io.github.splotycode.mosaik.util.listener.MultipleListenerHandler;
import lombok.Getter;

import java.util.function.Consumer;

public class MasterHost implements StatisticalHost {

    public static final HostProvider PROVIDER = MasterHost::new;

    public static final ConfigKey<Long> HEALTH_THRESHOLD = new ConfigKey<>("master.host.health_threshold", long.class, 8 * 1000L);

    private long lastUpdate;
    private MasterHealthCheck healthCheck = new MasterHealthCheck();
    @Getter private HostStatistics statistics;

    private CloudKit kit;
    private MosaikAddress address;

    public MasterHost(CloudKit kit, String address) {
        this.kit = kit;
        changeAddress(address);
    }

    public void changeAddress(String rawAddress) {
        MosaikAddress address = new MosaikAddress(rawAddress);
        handler.call(AddressChangeListener.class, (Consumer<AddressChangeListener>) listener -> listener.onChange(this.address, address));
        this.address = address;
    }

    private MultipleListenerHandler handler = new MultipleListenerHandler();

    @Override
    public void update(HostStatistics statistics) {
       this.statistics = statistics;
       lastUpdate = System.currentTimeMillis();
    }

    private class MasterHealthCheck implements HealthCheck {

        @Override
        public boolean isOnline() {
            long delay = System.currentTimeMillis() - lastUpdate;
            return delay <= kit.getConfig(HEALTH_THRESHOLD) + kit.getConfig(MasterService.DAEMON_STATS_DELAY);
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
