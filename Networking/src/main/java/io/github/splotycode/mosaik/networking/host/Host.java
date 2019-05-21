package io.github.splotycode.mosaik.networking.host;

import io.github.splotycode.mosaik.networking.healthcheck.HealthCheck;
import io.github.splotycode.mosaik.networking.util.MosaikAddress;
import io.github.splotycode.mosaik.util.listener.Listener;
import io.github.splotycode.mosaik.util.listener.ListenerHandler;

public interface Host {

    HealthCheck healthCheck();
    MosaikAddress address();

    ListenerHandler<Listener> handler();

    default boolean isOnline() {
        HealthCheck healthCheck = healthCheck();
        if (healthCheck == null) {
            throw new NullPointerException("healthCheck()");
        }
        return healthCheck.isOnline();
    }

}
