package me.david.davidlib.gamesngine.tick;

public interface TickExecutor {

    void runTick();

    void preTick();
    void postTick();

}
