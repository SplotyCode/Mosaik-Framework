package io.github.splotycode.mosaik.gameengine.tick;

public interface TickExecutor {

    void runTick();

    void preTick();
    void postTick();

}
