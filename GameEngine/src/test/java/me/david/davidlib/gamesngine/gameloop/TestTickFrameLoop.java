package me.david.davidlib.gamesngine.gameloop;

import me.david.davidlib.gamesngine.tick.TickExecutor;
import me.david.davidlib.utils.ThreadUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestTickFrameLoop {

    @Test
    @DisplayName("Accuracy")
    public void test() {
        TickFrameLoop loop = new TickFrameLoop(40, Integer.MAX_VALUE);
        loop.setRenderer(() -> ThreadUtil.sleep(3));
        loop.setTickExecutor(new TickExecutor() {
            @Override
            public void runTick() {
                ThreadUtil.sleep(5);
                System.out.println("FPS: " + loop.getCurrentFps() + " TPS: " + loop.getCurrentTps());
            }
            @Override public void preTick() {}
            @Override public void postTick() {}
        });
        loop.start();
    }
}
