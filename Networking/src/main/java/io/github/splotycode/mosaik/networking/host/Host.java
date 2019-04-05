package io.github.splotycode.mosaik.networking.host;

import io.github.splotycode.mosaik.networking.healthcheck.HealthCheck;

import java.net.InetAddress;

public interface Host {

    HealthCheck healthCheck();
    InetAddress address();

}
