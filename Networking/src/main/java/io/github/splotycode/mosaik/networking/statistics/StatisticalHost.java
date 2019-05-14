package io.github.splotycode.mosaik.networking.statistics;

import io.github.splotycode.mosaik.networking.host.Host;

public interface StatisticalHost extends Host {

    void update(HostStatistics statistics);
    HostStatistics getStatistics();

}
