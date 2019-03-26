package io.github.splotycode.mosaik.netty.healthcheck;

import java.net.SocketAddress;

public interface HealthCheck {

    SocketAddress getAddress();
    boolean isOnline();

}
