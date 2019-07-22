package io.github.splotycode.mosaik.startup.exception;

public class EnvironmentChangeException extends RuntimeException {

    public EnvironmentChangeException() {
    }

    public EnvironmentChangeException(String s) {
        super(s);
    }

    public EnvironmentChangeException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public EnvironmentChangeException(Throwable throwable) {
        super(throwable);
    }

    public EnvironmentChangeException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }

}
