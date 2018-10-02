package me.david.davidlib.gamesngine.gameloop;

import lombok.Getter;
import lombok.Setter;
import me.david.davidlib.gamesngine.render.Renderer;
import me.david.davidlib.gamesngine.tick.TickExecutor;

/**
 * This Game Loop while call Ticks at a consistent rate
 * It will try to catch up with tick if it is behind (up to 20 ticks)
 * It has as many render cycles as it can or as the fpsCap allow
 */

/*
 * TODO: be able to change the maximum catch up ticks
 */
public class TickFrameLoop implements GameLoop, Overloadable {

    @Getter private final int tps, fpsCap;
    @Getter private boolean running;
    @Getter private TickExecutor tickExecutor;
    @Getter @Setter private Renderer renderer;

    public TickFrameLoop(int tps, int fpsCap) {
        this.tps = tps;
        this.fpsCap = fpsCap;
    }

    public TickFrameLoop(int tps, int fpsCap, TickExecutor tickExecutor) {
        this.tps = tps;
        this.fpsCap = fpsCap;
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
        long lastFrameTime = 0;

        try {
            while (running) {
                long start = System.currentTimeMillis();

                try {
                    runTick();
                } catch (Exception ex) {
                    throw new GameLoopTickExepction("Exception while running current tick", ex);
                }

                long end = System.currentTimeMillis() - start;

                long renderTime = delay - end;
                if (renderTime < 0) {
                    for (int i = 0;i < Math.min(renderTime / delay, 20);i++) {
                        try {
                            runTick();
                        } catch (Exception ex) {
                            throw new GameLoopTickExepction("Exception while running current tick", ex);
                        }
                    }
                    cantKeepUp(delay, end);
                } else if (renderTime > 0) {
                    if (lastFrameTime == 0) lastFrameTime = renderTime;
                    long totalFrameTime = 0;
                    for (int i = 0; i < Math.min(renderTime / lastFrameTime, fpsCap); i++) {
                        long renderStart = System.currentTimeMillis();
                        try {
                            renderer.render();
                        } catch (Exception ex) {
                            throw new GameLoopTickExepction("Exception while drawing the screen");
                        }
                        lastFrameTime = Math.max(System.currentTimeMillis() - renderStart, 1);
                        totalFrameTime += lastFrameTime;
                        if (renderTime - totalFrameTime < lastFrameTime) {
                            break;
                        }
                    }
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
