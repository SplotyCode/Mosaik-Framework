package io.github.splotycode.mosaik.networking.healthcheck;

import java.net.SocketAddress;

public interface HealthCheck {

    SocketAddress getAddress();
    boolean isOnline();

}
