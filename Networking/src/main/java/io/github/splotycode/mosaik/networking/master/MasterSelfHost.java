package io.github.splotycode.mosaik.networking.master;

import io.github.splotycode.mosaik.networking.cloudkit.CloudKit;
import io.github.splotycode.mosaik.networking.config.ConfigKey;
import io.github.splotycode.mosaik.networking.healthcheck.HealthCheck;
import io.github.splotycode.mosaik.networking.healthcheck.StaticHealthCheck;
import io.github.splotycode.mosaik.networking.statistics.HostStatistics;
import io.github.splotycode.mosaik.networking.statistics.StatisticalHost;
import io.github.splotycode.mosaik.networking.statistics.UpdateLocalStatisticsTask;
import io.github.splotycode.mosaik.networking.util.IpResolver;
import io.github.splotycode.mosaik.util.ExceptionUtil;
import io.github.splotycode.mosaik.util.cache.Cache;
import io.github.splotycode.mosaik.util.cache.DefaultCaches;
import io.github.splotycode.mosaik.util.listener.DummyListenerHandler;
import io.github.splotycode.mosaik.util.listener.ListenerHandler;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MasterSelfHost implements StatisticalHost {

    public static final ConfigKey<Long> CACHE_TIME = new ConfigKey<>("master.self_stats_cache");

    private StaticHealthCheck healthCheck = new StaticHealthCheck(true);
    private IpResolver resolver;
    private Cache<HostStatistics> statistic;

    public MasterSelfHost(CloudKit cloudKit) {
        resolver = cloudKit.getLocalIpResolver();
        statistic = DefaultCaches.getTimeCache(cache -> UpdateLocalStatisticsTask.createPacket(cloudKit).toStatistics(), cloudKit.getConfig(CACHE_TIME));
    }

    @Override
    public void update(HostStatistics statistics) {
        this.statistic.setValue(statistics);
    }

    @Override
    public HostStatistics getStatistics() {
        return statistic.getValue();
    }

    @Override
    public HealthCheck healthCheck() {
        return healthCheck;
    }

    @Override
    public InetAddress address() {
        try {
            return InetAddress.getByName(resolver.getIpAddress());
        } catch (UnknownHostException e) {
            ExceptionUtil.throwRuntime(e);
            return null;
        }
    }

    @Override
    public ListenerHandler handler() {
        return DummyListenerHandler.DUMMY;
    }
}
