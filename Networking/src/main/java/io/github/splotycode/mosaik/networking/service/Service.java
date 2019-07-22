package io.github.splotycode.mosaik.networking.service;

import io.github.splotycode.mosaik.util.listener.Listener;

public interface Service extends Listener {

    default String displayName() {
        return getClass().getName();
    }

    void start();
    void stop();

    ServiceStatus getStatus();
    String statusMessage();

}
