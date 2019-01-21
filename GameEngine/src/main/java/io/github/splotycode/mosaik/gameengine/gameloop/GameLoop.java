package io.github.splotycode.mosaik.gameengine.gameloop;

import io.github.splotycode.mosaik.gameengine.tick.TickExecutor;

/**
 * A GameLoop is a the Main Loop in a Game
 * It is used to time the tick and executes TickExecutor#runTick
 * @see TickExecutor
 * @see TickFrameLoop
 * @see TPSGameLoop
 * @see UnInteruplyGameLoop
 */
public interface GameLoop {

    /**
     * Returns the current TickExecutor (null if not present)
     * @return the current TickExecutor
     */
    TickExecutor getTickExecutor();

    /**
     * Sets the Tick Executor for this loop.
     * You mal only use this method while the loop is paused/hasn't started
     * @param tickExecutor the new Tick Executor
     */
    void setTickExecutor(TickExecutor tickExecutor);

    /**
     * Pauses the Loop
     * You can use unPause to run it after you paused it
     */
    default void pause() {
        end();
    }

    /**
     * Unpauses a loop
     * The loop need to be paused before
     */
    default void unPause() {
        start(getTickExecutor());
    }

    /**
     * @return true if the look is running and false if it does'nt
     */
    boolean isRunning();

    /**
     * Starts the loop for the first time
     * @param tickExecutor the tickexecutor for the gameloop
     */
    void start(TickExecutor tickExecutor);

    /**
     * Starts the loop for the first time
     * You need to specify the TickExecutor first with setTickExecutor
     */
    void start();

    /**
     * End the Gameloop
     * You might not start it again if you end it
     */
    void end();

    /**
     * Calls a tick
     */
    void runTick();

    /**
     * Executed before a new tick begins
     */
    void preTick();

    /**
     * Executed after a tick
     */
    void postTick();

}
