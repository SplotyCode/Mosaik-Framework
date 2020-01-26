package io.github.splotycode.mosaik.networking.statistics.component;

import io.github.splotycode.mosaik.networking.statistics.network.CodecNetworkProcess;

public interface StatefulNetworkProcess extends CodecNetworkProcess {

    int FLAG_STARTING = 0x00000010;
    int FLAG_SHUTTING_DOWN = 0x00000020;

    boolean starting();
    boolean shuttingDown();

    @Override
    default void applyControlByte(int controlByte) {
        throw new IllegalStateException(getClass().getSimpleName() + " must override applyControlByte");
    }

    @Override
    default int calculateControlByte() {
        int controlByte = 0;
        if (starting()) {
            controlByte |= FLAG_STARTING;
        }
        if (shuttingDown()) {
            controlByte |= FLAG_SHUTTING_DOWN;
        }
        return controlByte;
    }
}
