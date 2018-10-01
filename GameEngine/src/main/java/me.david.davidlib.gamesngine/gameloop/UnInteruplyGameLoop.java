package me.david.davidlib.gamesngine.gameloop;

import lombok.Getter;
import me.david.davidlib.gamesngine.tick.TickExecutor;

public class UnInteruplyGameLoop implements GameLoop {

    @Getter private boolean running;
    @Getter private TickExecutor tickExecutor;

    public UnInteruplyGameLoop(TickExecutor tickExecutor) {
        this.tickExecutor = tickExecutor;
    }

    @Override
    public void start() {
        if (running) throw new GameLoopException("Game loop is already running");
        running = true;
        try {
            while (running) {
                try {
                    preTick();
                    runTick();
                    postTick();
                } catch (Exception ex) {
                    throw new GameLoopTickExepction("Exception while running current tick", ex);
                }

            }
        } catch (Exception ex) {
            throw new GameLoopException("Exception while running GameLoop", ex);
        }
    }

    @Override
    public void setTickExecutor(TickExecutor tickExecutor) {
        if (running) throw new GameLoopException("Can not set TickExecutor while GameLoop is running!");
        this.tickExecutor = tickExecutor;
    }

    @Override
    public void start(TickExecutor tickExecutor) {
        this.tickExecutor = tickExecutor;
        start();
    }

    @Override
    public void end() {
        if (!running) throw new GameLoopException("Game loop is not running");
        running = false;
    }

    @Override
    public final void runTick() {
        tickExecutor.runTick();
    }

    @Override public void preTick() {}
    @Override public void postTick() {}

}
