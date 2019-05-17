package io.github.splotycode.mosaik.networking.master;

import io.github.splotycode.mosaik.networking.cloudkit.CloudKit;
import io.github.splotycode.mosaik.networking.cloudkit.SelfHostProvider;
import io.github.splotycode.mosaik.networking.config.ConfigKey;
import io.github.splotycode.mosaik.networking.healthcheck.HealthCheck;
import io.github.splotycode.mosaik.networking.healthcheck.StaticHealthCheck;
import io.github.splotycode.mosaik.networking.statistics.HostStatistics;
import io.github.splotycode.mosaik.networking.statistics.StatisticalHost;
import io.github.splotycode.mosaik.networking.util.IpResolver;
import io.github.splotycode.mosaik.networking.util.MosaikAddress;
import io.github.splotycode.mosaik.util.cache.Cache;
import io.github.splotycode.mosaik.util.cache.DefaultCaches;
import io.github.splotycode.mosaik.util.listener.DummyListenerHandler;
import io.github.splotycode.mosaik.util.listener.ListenerHandler;

public class MasterSelfHost implements StatisticalHost {

    public static final SelfHostProvider PROVIDER = MasterSelfHost::new;

    public static final ConfigKey<Long> CACHE_TIME = new ConfigKey<>("master.self_stats_cache", long.class);

    private StaticHealthCheck healthCheck = new StaticHealthCheck(true);
    private IpResolver resolver;
    private Cache<HostStatistics> statistic;
    private CloudKit cloudKit;
    private HostStatistics set;

    public MasterSelfHost(CloudKit cloudKit) {
        this.cloudKit = cloudKit;
        resolver = cloudKit.getLocalIpResolver();
    }

    @Override
    public void update(HostStatistics statistics) {
        if (this.statistic == null) {
            set = statistics;
        } else {
            this.statistic.setValue(statistics);
        }
    }

    private void createCache() {
        statistic = DefaultCaches.getTimeCache(cache -> HostStatistics.current(cloudKit), cloudKit.getConfig(CACHE_TIME));
        if (set != null) {
            statistic.setValue(set);
        }
    }

    @Override
    public HostStatistics getStatistics() {
        createCache();
        return statistic.getValue();
    }

    @Override
    public HealthCheck healthCheck() {
        return healthCheck;
    }

    @Override
    public MosaikAddress address() {
        return resolver.getIpAddress();
    }

    @Override
    public ListenerHandler handler() {
        return DummyListenerHandler.DUMMY;
    }
}
