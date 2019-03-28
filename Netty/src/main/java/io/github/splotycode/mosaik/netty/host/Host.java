package io.github.splotycode.mosaik.netty.host;

import io.github.splotycode.mosaik.netty.healthcheck.HealthCheck;

import java.net.InetAddress;

public interface Host {

    HealthCheck healthCheck();
    InetAddress address();

}
