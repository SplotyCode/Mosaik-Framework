package io.github.splotycode.mosaik.networking.host;

import io.github.splotycode.mosaik.networking.healthcheck.HealthCheck;
import io.github.splotycode.mosaik.util.listener.ListenerHandler;

import java.net.InetAddress;

public interface Host {

    HealthCheck healthCheck();
    InetAddress address();

    ListenerHandler handler();

}
