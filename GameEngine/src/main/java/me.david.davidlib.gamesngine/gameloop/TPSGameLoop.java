package me.david.davidlib.gamesngine.gameloop;

import lombok.Getter;
import me.david.davidlib.gamesngine.tick.TickExecutor;
import me.david.davidlib.util.ThreadUtil;

public class TPSGameLoop implements GameLoop, Overloadable {

    @Getter private final int tps;
    @Getter private boolean running;
    @Getter private TickExecutor tickExecutor;

    public TPSGameLoop(int tps) {
        this.tps = tps;
    }

    public TPSGameLoop(int tps, TickExecutor tickExecutor) {
        this.tps = tps;
        this.tickExecutor = tickExecutor;
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
    public void start() {
        if (running) throw new GameLoopException("Game loop is already running");
        running = true;

        long delay = 1000 / tps;

        try {
            while (running) {
                long start = System.currentTimeMillis();

                try {
                    runTick();
                } catch (Exception ex) {
                    throw new GameLoopTickExepction("Exception while running current tick", ex);
                }

                long end = System.currentTimeMillis() - start;

                long sleepDelay = delay - end;
                if (sleepDelay > 0) {
                    ThreadUtil.sleep(sleepDelay);
                } else if (sleepDelay < 0) {
                    cantKeepUp(delay, end);
                }
            }
        } catch (Exception ex) {
            throw new GameLoopException("Exception while running GameLoop", ex);
        }
    }

    @Override
    public void end() {
        if (!running) throw new GameLoopException("Game loop is not running");
        running = false;
    }

    @Override
    public final void runTick() {
        preTick();
        tickExecutor.runTick();
        postTick();
    }

    @Override public void preTick() {}
    @Override public void postTick() {}

    @Override public void cantKeepUp(long normal, long current) {}

}
