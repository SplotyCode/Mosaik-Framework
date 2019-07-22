package io.github.splotycode.mosaik.networking.statistics;

import io.github.splotycode.mosaik.networking.component.INetworkProcess;
import io.github.splotycode.mosaik.networking.master.host.MasterHost;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Instance implements INetworkProcess {

    private int connections, port;
    private StatisticalHost host;
    private boolean shuttingDown;
    private String service;

    @Override
    public int port() {
        return port;
    }

    @Override
    public boolean running() {
        return !shuttingDown;
    }

    @Override
    public void stop() {
        shuttingDown = true;
        if (host instanceof MasterHost) {
            ((MasterHost) host).stopService(service, port);
        }
    }

    public HostStatistics getHostStatistics() {
        return host.getStatistics();
    }

}
