package io.github.splotycode.mosaik.networking.loadbalance;

import io.github.splotycode.mosaik.networking.cloudkit.CloudKit;
import io.github.splotycode.mosaik.networking.component.tcp.TCPServer;
import io.github.splotycode.mosaik.networking.component.template.ServerTemplate;
import io.github.splotycode.mosaik.networking.master.manage.MasterInstanceService;
import io.github.splotycode.mosaik.networking.reverseproxy.ClientProxyHandler;
import io.github.splotycode.mosaik.networking.service.ServiceStatus;

import static io.github.splotycode.mosaik.networking.service.ServiceStatus.*;

public abstract class LoadBalancerService extends MasterInstanceService<TCPServer<? extends TCPServer>> {

    private ServiceStatus status = ServiceStatus.UNKNOWN;

    private ServerTemplate<TCPServer<? extends TCPServer>> template =
                    ServerTemplate.tcp()
                    .setDisplayName("Load Balancer")
                    .childHandler(1, "Proxy Front-Load", ClientProxyHandler.class, () -> new ClientProxyHandler(getRedirectTemplacte()));

    public abstract ServerTemplate getRedirectTemplacte();

    public LoadBalancerService(String prefix, CloudKit kit) {
        super(kit, prefix);
    }

    @Override
    public void start() {
        status = STARTING;
        super.start();
        status = RUNNING;
    }

    @Override
    public void stop() {
        status = STOPPING;
        super.stop();
        status = STOPPED;
    }

    @Override
    protected TCPServer<? extends TCPServer> createComponent(int port) {
        return template.createComponent().port(port).bind();
    }

    @Override
    public ServiceStatus getStatus() {
        return status;
    }

    @Override
    public String statusMessage() {
        return null;
    }
}
