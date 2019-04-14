package io.github.splotycode.mosaik.networking.host;

import io.github.splotycode.mosaik.networking.healthcheck.HealthCheck;
import io.github.splotycode.mosaik.networking.healthcheck.StaticHealthCheck;
import io.github.splotycode.mosaik.networking.util.IpResolver;
import io.github.splotycode.mosaik.util.ExceptionUtil;
import io.github.splotycode.mosaik.util.listener.ListenerHandler;
import io.github.splotycode.mosaik.util.listener.MultipleListenerHandler;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class SelfHost implements Host {

    private InetAddress address;
    private StaticHealthCheck healthCheck = new StaticHealthCheck(true, new InetSocketAddress("localhost", 80));

    private MultipleListenerHandler handler = new MultipleListenerHandler();

    private boolean localAddress;

    public SelfHost(boolean localAddress) {
        this.localAddress = localAddress;
        try {
            address = localAddress ? InetAddress.getLocalHost() : InetAddress.getByName(IpResolver.GLOBAL.getIpAddress());
        } catch (UnknownHostException e) {
            ExceptionUtil.throwRuntime(e);
        }
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
