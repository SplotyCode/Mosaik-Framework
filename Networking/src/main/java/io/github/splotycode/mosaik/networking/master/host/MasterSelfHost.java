package io.github.splotycode.mosaik.networking.master.host;

import io.github.splotycode.mosaik.networking.cloudkit.CloudKit;
import io.github.splotycode.mosaik.networking.cloudkit.SelfHostProvider;
import io.github.splotycode.mosaik.networking.config.ConfigKey;
import io.github.splotycode.mosaik.networking.healthcheck.HealthCheck;
import io.github.splotycode.mosaik.networking.healthcheck.StaticHealthCheck;
import io.github.splotycode.mosaik.networking.master.MasterService;
import io.github.splotycode.mosaik.networking.master.manage.MasterInstanceService;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import io.github.splotycode.mosaik.networking.service.Service;
import io.github.splotycode.mosaik.networking.statistics.HostStatistics;
import io.github.splotycode.mosaik.networking.statistics.component.AbstractStatisticalHost;
import io.github.splotycode.mosaik.networking.statistics.local.LocalHostStatistics;
import io.github.splotycode.mosaik.networking.statistics.remote.RemoveHostStatistics;
import io.github.splotycode.mosaik.networking.util.IpResolver;
import io.github.splotycode.mosaik.networking.util.MosaikAddress;
import io.github.splotycode.mosaik.util.cache.Cache;
import io.github.splotycode.mosaik.util.cache.DefaultCaches;
import io.github.splotycode.mosaik.util.listener.DummyListenerHandler;
import io.github.splotycode.mosaik.util.listener.ListenerHandler;

public class MasterSelfHost extends AbstractStatisticalHost implements MasterHost {

    public static final SelfHostProvider PROVIDER = MasterSelfHost::new;

    private IpResolver resolver;
    private Cache<RemoveHostStatistics> statistic;
    private CloudKit cloudKit;
    private RemoveHostStatistics set;
    private MasterService masterService;

    public MasterSelfHost(CloudKit cloudKit) {
        this.cloudKit = cloudKit;
        resolver = cloudKit.getLocalIpResolver();
    }

    protected MasterService masterService() {
        if (masterService == null) {
            masterService = cloudKit.getServiceByClass(MasterService.class);
        }
        return masterService;
    }

    @Override
    public String toString() {
        return "Local-" + address().asString();
    }

    @Override
    public void update(RemoveHostStatistics statistics) {
        if (this.statistic == null) {
            set = statistics;
        } else {
            this.statistic.setValue(statistics);
        }
    }

    private void createCache() {
        if (statistic == null) {
            statistic = DefaultCaches.getTimeCache(cache -> RemoveHostStatistics.current(cloudKit), cloudKit.getConfig(CACHE_TIME));
            if (set != null) {
                statistic.setValue(set);
            }
        }
    }

    @Override
    public RemoveHostStatistics getStatistics() {
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
    public void sendPacket(SerializedPacket packet) {
        masterService().sendSelf(packet);
    }

    @Override
    public CloudKit cloudKit() {
        return cloudKit;
    }

    @Override
    public HostStatistics createStatistics() {
        return new LocalHostStatistics(this, cloudKit);
    }
}
