package io.github.splotycode.mosaik.gameengine.gameloop;

import io.github.splotycode.mosaik.gameengine.tick.TickExecutor;
import io.github.splotycode.mosaik.util.ThreadUtil;
import io.github.splotycode.mosaik.util.logger.Logger;
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
