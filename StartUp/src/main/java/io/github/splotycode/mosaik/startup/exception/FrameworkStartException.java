package io.github.splotycode.mosaik.startup.exception;

public class FrameworkStartException extends RuntimeException {

    public FrameworkStartException() {
    }

    public FrameworkStartException(String s) {
        super(s);
    }

    public FrameworkStartException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public FrameworkStartException(Throwable throwable) {
        super(throwable);
    }

    public FrameworkStartException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }

}
