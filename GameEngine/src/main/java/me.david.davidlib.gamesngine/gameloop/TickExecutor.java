package me.david.davidlib.gamesngine.gameloop;

public interface TickExecutor {

    void runTick();

    void postTick();
    void preTick();

}
