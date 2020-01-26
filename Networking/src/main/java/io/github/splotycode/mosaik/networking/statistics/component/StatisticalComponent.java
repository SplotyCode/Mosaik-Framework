package io.github.splotycode.mosaik.networking.statistics.component;

import io.github.splotycode.mosaik.networking.statistics.IStatistics;

public interface StatisticalComponent<S extends IStatistics> {

    S statistics();
    S createStatistics();

}
