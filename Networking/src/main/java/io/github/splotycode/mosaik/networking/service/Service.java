package io.github.splotycode.mosaik.networking.service;

public interface Service {

    default String displayName() {
        return getClass().getName();
    }

    void start();
    void stop();

    ServiceStatus getStatus();
    String statusMessage();

}