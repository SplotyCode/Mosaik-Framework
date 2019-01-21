package io.github.splotycode.mosaik.gameengine.tick;

public class TickRegistryExcetion extends RuntimeException {

    public TickRegistryExcetion() {
    }

    public TickRegistryExcetion(String s) {
        super(s);
    }

    public TickRegistryExcetion(String s, Throwable throwable) {
        super(s, throwable);
    }

    public TickRegistryExcetion(Throwable throwable) {
        super(throwable);
    }

    public TickRegistryExcetion(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
