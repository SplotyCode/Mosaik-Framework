package io.github.splotycode.mosaik.util.init;

public class AlreadyInitailizedException extends RuntimeException {

    public AlreadyInitailizedException() {
    }

    public AlreadyInitailizedException(String s) {
        super(s);
    }

    public AlreadyInitailizedException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AlreadyInitailizedException(Throwable throwable) {
        super(throwable);
    }

    public AlreadyInitailizedException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
