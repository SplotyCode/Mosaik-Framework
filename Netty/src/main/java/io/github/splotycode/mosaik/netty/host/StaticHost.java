package io.github.splotycode.mosaik.netty.host;

import io.github.splotycode.mosaik.netty.healthcheck.HealthCheck;
import io.github.splotycode.mosaik.netty.healthcheck.PingingHealthCheck;
import io.github.splotycode.mosaik.util.task.TaskExecutor;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class StaticHost implements Host {

    private HealthCheck healthCheck;
    private InetAddress address;

    public StaticHost(InetAddress address, int checkPort, TaskExecutor executor, int interval, int timeout, int successThreshold, int failThreshold) {
        healthCheck = new PingingHealthCheck(new InetSocketAddress(address.getHostAddress(), checkPort), executor, interval, timeout, successThreshold, failThreshold);
    }

    @Override
    public HealthCheck healthCheck() {
        return healthCheck;
    }

    @Override
    public InetAddress address() {
        return address;
    }
}
