package io.github.splotycode.mosaik.networking.host;

import io.github.splotycode.mosaik.networking.healthcheck.HealthCheck;
import io.github.splotycode.mosaik.networking.healthcheck.PingingHealthCheck;
import io.github.splotycode.mosaik.util.listener.ListenerHandler;
import io.github.splotycode.mosaik.util.listener.MultipleListenerHandler;
import io.github.splotycode.mosaik.util.task.TaskExecutor;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.function.Consumer;

public class StaticHost implements Host {

    private HealthCheck healthCheck;
    private InetAddress address;
    private MultipleListenerHandler handler = new MultipleListenerHandler();

    public StaticHost(InetAddress address, int checkPort, TaskExecutor executor, int interval, int timeout, int successThreshold, int failThreshold) {
        healthCheck = new PingingHealthCheck(new InetSocketAddress(address.getHostAddress(), checkPort), executor, interval, timeout, successThreshold, failThreshold);
    }

    private void setAddress(InetAddress address) {
        handler.call(AddressChangeListener.class, (Consumer<AddressChangeListener>) listener -> listener.onChange(this.address, address));
        this.address = address;
    }

    @Override
    public HealthCheck healthCheck() {
        return healthCheck;
    }

    @Override
    public InetAddress address() {
        return address;
    }

    @Override
    public ListenerHandler handler() {
        return handler;
    }
}
