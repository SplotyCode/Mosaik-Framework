package me.david.davidlib.gamesngine.gameloop;

import lombok.Getter;
import lombok.Setter;
import me.david.davidlib.gamesngine.render.Renderer;
import me.david.davidlib.gamesngine.tick.TickExecutor;
import me.david.davidlib.util.ThreadUtil;

/**
 * This Game Loop while call Ticks at a consistent rate
 * It will try to catch up with tick if it is behind (up to 20 ticks)
 * It has as many render cycles as it can or as the fpsCap allows
 */

/*
 * TODO: be able to change the maximum catch up ticks
 */
public class TickFrameLoop implements GameLoop, Overloadable {

    @Getter private final int tps, fpsCap;
    @Getter private boolean running;
    @Getter private TickExecutor tickExecutor;
    @Getter @Setter private Renderer renderer;

    @Getter private int currentFps, currentTps;

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

        int renderCycles = 0, tickCycles = 0;
        long lastStatReset = System.currentTimeMillis();

        try {
            while (running) {
                if (System.currentTimeMillis() - lastStatReset > 1000) {
                    currentTps = tickCycles;
                    currentFps = renderCycles;
                    renderCycles = 0;
                    tickCycles = 0;
                    lastStatReset = System.currentTimeMillis();
                }
                long start = System.currentTimeMillis();

                try {
                    runTick();
                    tickCycles++;
                } catch (Exception ex) {
                    throw new GameLoopTickExepction("Exception while running current tick", ex);
                }

                long end = System.currentTimeMillis() - start;

                long renderTime = delay - end;
                if (renderTime < 0) {
                    for (int i = 0;i < Math.min(Math.abs(renderTime) / delay, 20);i++) {
                        try {
                            runTick();
                            tickCycles++;
                        } catch (Exception ex) {
                            throw new GameLoopTickExepction("Exception while running current tick", ex);
                        }
                    }
                    cantKeepUp(delay, end);
                } else if (renderTime > 0) {
                    if (lastFrameTime == 0) lastFrameTime = renderTime;
                    long totalFrameTime = 0;
                    for (int i = 0; i < renderTime / lastFrameTime; i++) {
                        if (renderCycles >= fpsCap) {
                            break;
                        }
                        long renderStart = System.currentTimeMillis();
                        try {
                            renderer.render();
                            renderCycles++;
                        } catch (Exception ex) {
                            throw new GameLoopTickExepction("Exception while drawing the screen", ex);
                        }
                        lastFrameTime = Math.max(System.currentTimeMillis() - renderStart, 1);
                        totalFrameTime += lastFrameTime;
                        if (renderTime - totalFrameTime < lastFrameTime) {
                            break;
                        }
                    }
                    if (totalFrameTime < renderTime) {
                        ThreadUtil.sleep(renderTime - totalFrameTime);
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
