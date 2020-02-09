package io.github.splotycode.mosaik.networking.master.host;

import io.github.splotycode.mosaik.networking.cloudkit.CloudKit;
import io.github.splotycode.mosaik.networking.host.SelfHostProvider;
import io.github.splotycode.mosaik.networking.healthcheck.HealthCheck;
import io.github.splotycode.mosaik.networking.healthcheck.StaticHealthCheck;
import io.github.splotycode.mosaik.networking.master.MasterService;
import io.github.splotycode.mosaik.networking.master.manage.MasterInstanceService;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import io.github.splotycode.mosaik.networking.service.Service;
import io.github.splotycode.mosaik.networking.statistics.HostStatistics;
import io.github.splotycode.mosaik.networking.statistics.component.AbstractStatisticalHost;
import io.github.splotycode.mosaik.networking.statistics.local.LocalHostStatistics;
import io.github.splotycode.mosaik.networking.util.IpResolver;
import io.github.splotycode.mosaik.networking.util.MosaikAddress;
import io.github.splotycode.mosaik.util.listener.DummyListenerHandler;
import io.github.splotycode.mosaik.util.listener.ListenerHandler;

public class MasterSelfHost extends AbstractStatisticalHost implements MasterHost {

    public static final SelfHostProvider PROVIDER = MasterSelfHost::new;

    private IpResolver resolver;
    private CloudKit cloudKit;
    private MasterService masterService;

    public MasterSelfHost(CloudKit cloudKit) {
        this.cloudKit = cloudKit;
        resolver = cloudKit.getLocalIpResolver();
        tryInitialize();
    }

    @Override
    public String toString() {
        return "Local-" + address().asString();
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
        assert masterService != null : "MasterService not ready";
        masterService.sendSelf(packet);
    }

    @Override
    public void initialize(MasterService masterService) {
        this.masterService = masterService;
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
