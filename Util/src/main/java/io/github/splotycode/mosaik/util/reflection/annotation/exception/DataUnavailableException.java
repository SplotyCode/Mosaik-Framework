package io.github.splotycode.mosaik.util.reflection.annotation.exception;

public class DataUnavailableException extends RuntimeException {

    public DataUnavailableException() {
    }

    public DataUnavailableException(String s) {
        super(s);
    }

    public DataUnavailableException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public DataUnavailableException(Throwable throwable) {
        super(throwable);
    }

    public DataUnavailableException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }

}
