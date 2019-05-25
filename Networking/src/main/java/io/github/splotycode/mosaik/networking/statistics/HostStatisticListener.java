package io.github.splotycode.mosaik.networking.statistics;

import io.github.splotycode.mosaik.util.listener.Listener;

public interface HostStatisticListener extends Listener {

    void update(StatisticalHost host);

}
