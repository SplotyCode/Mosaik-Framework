package io.github.splotycode.mosaik.netty;

import io.github.splotycode.mosaik.util.listener.Listener;

public interface InstanceListener extends Listener {

    void startServer(int port, boolean start);
    void startInstances(int instances);

}
