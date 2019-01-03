package me.david.davidlib.util.core.gamesngine.gameloop;

import me.david.davidlib.util.logger.Logger;
import me.david.davidlib.gamesngine.gameloop.TickFrameLoop;
import me.david.davidlib.gamesngine.tick.TickExecutor;
import me.david.davidlib.utils.ThreadUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestTickFrameLoop {

    static {

    }

    private Logger logger = Logger.getInstance(getClass());

    @Test
    @DisplayName("Accuracy")
    public void test() {
        TickFrameLoop loop = new TickFrameLoop(40, Integer.MAX_VALUE);
        loop.setRenderer(() -> ThreadUtil.sleep(3));
        loop.setTickExecutor(new TickExecutor() {
            @Override
            public void runTick() {
                ThreadUtil.sleep(5);
                logger.info("FPS: " + loop.getCurrentFps() + " TPS: " + loop.getCurrentTps());
                if (loop.getCurrentFps() > 5) {
                    System.exit(0);
                }
            }
            @Override public void preTick() {}
            @Override public void postTick() {}
        });
        loop.start();
    }
}
