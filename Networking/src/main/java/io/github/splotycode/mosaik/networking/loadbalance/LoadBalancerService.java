package io.github.splotycode.mosaik.networking.loadbalance;

import io.github.splotycode.mosaik.networking.cloudkit.CloudKit;
import io.github.splotycode.mosaik.networking.component.tcp.TCPServer;
import io.github.splotycode.mosaik.networking.component.template.ServerTemplate;
import io.github.splotycode.mosaik.networking.master.manage.MasterInstanceService;
import io.github.splotycode.mosaik.networking.reverseproxy.StaticClientProxyHandler;
import io.github.splotycode.mosaik.networking.service.ServiceStatus;
import io.github.splotycode.mosaik.networking.statistics.HostStatisticListener;
import io.github.splotycode.mosaik.networking.statistics.StatisticalHost;
import io.github.splotycode.mosaik.util.task.types.CompressingTask;
import lombok.Getter;

import static io.github.splotycode.mosaik.networking.service.ServiceStatus.*;

public class LoadBalancerService extends MasterInstanceService<TCPServer<? extends TCPServer>> implements HostStatisticListener {

    private ServiceStatus status = ServiceStatus.UNKNOWN;
    @Getter private LoadBalanceStrategy strategy;
    @Getter private String redirectService;

    private ServerTemplate<TCPServer<? extends TCPServer>> template =
                    ServerTemplate.tcp()
                    .setDisplayName("Load Balancer")
                    .childHandler(1, "Proxy Front-Load", StaticClientProxyHandler.class, () -> new StaticClientProxyHandler(strategy.nextServer()));

    public LoadBalancerService(String prefix, CloudKit kit, LoadBalanceStrategy strategy, String redirectService) {
        super(kit, prefix);
        this.strategy = strategy;
        this.redirectService = redirectService;
    }

    private long strategyTaskID;
    private CompressingTask strategyTask = new CompressingTask(displayName() + " strategy", () -> strategy.update(this), 8 * 1000, 5 * 1000);



    @Override
    public void update(StatisticalHost host) {
        strategyTask.requestUpdate();
    }

    @Override
    public void start() {
        status = STARTING;
        strategyTaskID = kit.getLocalTaskExecutor().runTask(strategyTask);
        super.start();
        status = RUNNING;
    }

    @Override
    public void stop() {
        status = STOPPING;
        kit.getLocalTaskExecutor().stopTask(strategyTaskID);
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

}
