package io.github.splotycode.mosaik.networking.statistics;

import io.github.splotycode.mosaik.networking.master.host.MasterHost;
import io.github.splotycode.mosaik.networking.service.Service;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Instance {

    private int connections, port;
    private StatisticalHost host;
    private boolean shuttingDown;
    private String service;

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
