package me.david.davidlib.util.core.netty;

import me.david.davidlib.util.listener.Listener;

public interface InstanceListener extends Listener {

    void startServer(int port, boolean start);
    void startInstances(int instances);

}
