package io.github.splotycode.mosaik.networking.statistics.local;

import io.github.splotycode.mosaik.networking.statistics.IStatistics;

public abstract class AbstractLocalStatistics implements IStatistics {

    @Override
    public boolean isLocal() {
        return true;
    }

    @Override
    public long lastUpdate() {
        return System.currentTimeMillis();
    }
}
