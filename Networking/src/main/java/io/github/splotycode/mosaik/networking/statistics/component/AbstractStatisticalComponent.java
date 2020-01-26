package io.github.splotycode.mosaik.networking.statistics.component;

import io.github.splotycode.mosaik.networking.statistics.IStatistics;

public abstract class AbstractStatisticalComponent<S extends IStatistics> implements StatisticalComponent<S> {

    protected volatile S statistics;

    @Override
    public S statistics() {
        if (statistics == null) {
            synchronized (this) {
                if (statistics == null) {
                    statistics = createStatistics();
                }
            }
        }
        return statistics;
    }
}
