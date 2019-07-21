package io.github.splotycode.mosaik.networking.master.manage;

import io.github.splotycode.mosaik.networking.component.INetworkProcess;

public class SystemProcess implements INetworkProcess {

    protected int port;
    protected Process process;

    public SystemProcess(int port) {
        this.port = port;
    }

    @Override
    public void stop() {
        process.destroy();
    }

    @Override
    public int port() {
        return port;
    }

    @Override
    public boolean running() {
        return process != null && process.isAlive();
    }
}
