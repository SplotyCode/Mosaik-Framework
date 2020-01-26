package io.github.splotycode.mosaik.networking.component;

import io.github.splotycode.mosaik.networking.host.Host;
import io.github.splotycode.mosaik.networking.statistics.HostStatistics;
import io.github.splotycode.mosaik.networking.statistics.component.StatisticalHost;

public interface INetworkProcess {

    default Host host() {
        return null;
    }

    default HostStatistics hostStatistics() {
        Host host = host();
        if (host instanceof StatisticalHost) {
            return ((StatisticalHost) host).statistics();
        }
        return null;
    }

    void stop();

    int port();

    boolean running();

    default int connectionCount() {
        return -1;
    }

}
