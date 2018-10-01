package me.david.davidlib.gamesngine.gameloop;

import lombok.Getter;

public class UnInteruplyGameLoop implements GameLoop {

    @Getter private boolean running;
    @Getter private TickExecutor tickExecutor;

    public UnInteruplyGameLoop(TickExecutor tickExecutor) {
        this.tickExecutor = tickExecutor;
    }

    @Override
    public void start() {
        if (running) throw new GameLoopExecption("Game loop is already running");
        running = true;
        while (running) {
            preTick();
            runTick();
            postTick();
        }
    }

    @Override
    public void setTickExecutor(TickExecutor tickExecutor) {
        if (running) throw new GameLoopExecption("Can not set TickExecutor while GameLoop is running!");
        this.tickExecutor = tickExecutor;
    }

    @Override
    public void start(TickExecutor tickExecutor) {
        this.tickExecutor = tickExecutor;
        start();
    }

    @Override
    public void end() {
        if (!running) throw new GameLoopExecption("Game loop is not running");
        running = false;
    }

    @Override
    public final void runTick() {
        tickExecutor.runTick();
    }

    @Override public void preTick() {}
    @Override public void postTick() {}

}
