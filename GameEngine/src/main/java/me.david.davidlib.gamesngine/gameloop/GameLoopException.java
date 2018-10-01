package me.david.davidlib.gamesngine.gameloop;

public class GameLoopException extends RuntimeException {

    public GameLoopException() {
    }

    public GameLoopException(String s) {
        super(s);
    }

    public GameLoopException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public GameLoopException(Throwable throwable) {
        super(throwable);
    }

    public GameLoopException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
