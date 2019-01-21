package io.github.splotycode.mosaik.runtime.startup;

public class BootException extends RuntimeException {

    public BootException() {
    }

    public BootException(String s) {
        super(s);
    }

    public BootException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public BootException(Throwable throwable) {
        super(throwable);
    }

    public BootException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
