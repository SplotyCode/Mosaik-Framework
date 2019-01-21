package io.github.splotycode.mosaik.gameengine.gameloop;

public class GameLoopTickExepction extends GameLoopException {

    public GameLoopTickExepction() {
    }

    public GameLoopTickExepction(String s) {
        super(s);
    }

    public GameLoopTickExepction(String s, Throwable throwable) {
        super(s, throwable);
    }

    public GameLoopTickExepction(Throwable throwable) {
        super(throwable);
    }

    public GameLoopTickExepction(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
