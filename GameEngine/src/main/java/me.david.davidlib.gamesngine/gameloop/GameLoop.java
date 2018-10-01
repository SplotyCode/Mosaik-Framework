package me.david.davidlib.gamesngine.gameloop;

import me.david.davidlib.gamesngine.tick.TickExecutor;

public interface GameLoop {

    TickExecutor getTickExecutor();
    void setTickExecutor(TickExecutor tickExecutor);

    default void pause() {
        end();
    }

    default void unPause() {
        start(getTickExecutor());
    }

    boolean isRunning();

    void start(TickExecutor tickExecutor);
    void start();
    void end();

    void runTick();
    void preTick();
    void postTick();

}
