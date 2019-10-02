package io.github.splotycode.mosaik.networking.master.host;

import io.github.splotycode.mosaik.networking.cloudkit.CloudKit;
import io.github.splotycode.mosaik.networking.cloudkit.SelfHostProvider;
import io.github.splotycode.mosaik.networking.config.ConfigKey;
import io.github.splotycode.mosaik.networking.healthcheck.HealthCheck;
import io.github.splotycode.mosaik.networking.healthcheck.StaticHealthCheck;
import io.github.splotycode.mosaik.networking.master.manage.MasterInstanceService;
import io.github.splotycode.mosaik.networking.service.Service;
import io.github.splotycode.mosaik.networking.statistics.HostStatistics;
import io.github.splotycode.mosaik.networking.util.IpResolver;
import io.github.splotycode.mosaik.networking.util.MosaikAddress;
import io.github.splotycode.mosaik.util.cache.Cache;
import io.github.splotycode.mosaik.util.cache.DefaultCaches;
import io.github.splotycode.mosaik.util.listener.DummyListenerHandler;
import io.github.splotycode.mosaik.util.listener.ListenerHandler;

public class MasterSelfHost implements MasterHost {

    public static final SelfHostProvider PROVIDER = MasterSelfHost::new;

    public static final ConfigKey<Long> CACHE_TIME = new ConfigKey<>("master.self_stats_cache", long.class, 2000L);

    private IpResolver resolver;
    private Cache<HostStatistics> statistic;
    private CloudKit cloudKit;
    private HostStatistics set;

    public MasterSelfHost(CloudKit cloudKit) {
        this.cloudKit = cloudKit;
        resolver = cloudKit.getLocalIpResolver();
    }

    @Override
    public String toString() {
        return "Local-" + address().asString();
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
        if (statistic == null) {
            statistic = DefaultCaches.getTimeCache(cache -> HostStatistics.current(cloudKit), cloudKit.getConfig(CACHE_TIME));
            if (set != null) {
                statistic.setValue(set);
            }
        }
    }

    @Override
    public HostStatistics getStatistics() {
        createCache();
        return statistic.getValue();
    }

    @Override
    public HealthCheck healthCheck() {
        return StaticHealthCheck.TRUE;
    }

    @Override
    public MosaikAddress address() {
        return resolver.getIpAddress();
    }

    @Override
    public ListenerHandler handler() {
        return DummyListenerHandler.DUMMY;
    }

    @Override
    public void startNewInstance(Service service) {
        if (service instanceof MasterInstanceService) {
            ((MasterInstanceService) service).getLocalManager().startNewInstance();
        }
    }

    @Override
    public void stopService(String rawService, int port) {
        Service service = cloudKit.getServiceByName(rawService);
        if (service instanceof MasterInstanceService) {
            ((MasterInstanceService) service).getLocalManager().stop(port);
        }
    }

    @Override
    public CloudKit cloudKit() {
        return cloudKit;
    }
}
