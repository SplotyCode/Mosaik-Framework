package me.david.davidlib.gamesngine.gameloop;

public class GameLoopExecption extends RuntimeException {

    public GameLoopExecption() {
    }

    public GameLoopExecption(String s) {
        super(s);
    }

    public GameLoopExecption(String s, Throwable throwable) {
        super(s, throwable);
    }

    public GameLoopExecption(Throwable throwable) {
        super(throwable);
    }

    public GameLoopExecption(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
