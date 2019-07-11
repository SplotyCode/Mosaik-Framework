package io.github.splotycode.mosaik.networking.component;

public interface INetworkProcess {

    void stop();

    int port();

    boolean running();

    default int connectionCount() {
        return -1;
    }

}
