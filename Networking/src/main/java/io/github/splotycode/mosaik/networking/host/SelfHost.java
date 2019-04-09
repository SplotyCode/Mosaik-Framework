package io.github.splotycode.mosaik.networking.host;

import io.github.splotycode.mosaik.networking.healthcheck.HealthCheck;
import io.github.splotycode.mosaik.networking.healthcheck.StaticHealthCheck;
import io.github.splotycode.mosaik.util.ExceptionUtil;
import io.github.splotycode.mosaik.util.listener.ListenerHandler;
import io.github.splotycode.mosaik.util.listener.MultipleListenerHandler;
import lombok.Getter;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class SelfHost implements Host {

    @Getter private InetAddress address;
    private StaticHealthCheck healthCheck = new StaticHealthCheck(true, new InetSocketAddress("localhost", 80));

    private MultipleListenerHandler handler = new MultipleListenerHandler();

    public SelfHost() {
        try {
            address = InetAddress.getLocalHost();
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
