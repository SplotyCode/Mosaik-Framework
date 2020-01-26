package io.github.splotycode.mosaik.networking.statistics.remote;

import io.github.splotycode.mosaik.networking.statistics.IStatistics;

public abstract class AbstractRemoteStatistics implements IStatistics {

    protected long lastUpdate;

    public void triggerUpdate() {
        lastUpdate = System.currentTimeMillis();
    }

    @Override
    public boolean isLocal() {
        return false;
    }

    @Override
    public long lastUpdate() {
        return lastUpdate;
    }
}
