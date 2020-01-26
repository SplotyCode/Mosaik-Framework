package io.github.splotycode.mosaik.networking.statistics.component;

import lombok.Getter;

@Getter
public abstract class AbstractStatefulProcess implements StatefulNetworkProcess {

    protected boolean starting;
    protected boolean shuttingDown;

    @Override
    public boolean running() {
        return !shuttingDown && !starting;
    }

    @Override
    public void applyControlByte(int controlByte) {
        starting = (controlByte & FLAG_STARTING) != 0;
        shuttingDown = (controlByte & FLAG_SHUTTING_DOWN) != 0;
    }

    @Override
    public boolean starting() {
        return starting;
    }

    @Override
    public boolean shuttingDown() {
        return shuttingDown;
    }

}
