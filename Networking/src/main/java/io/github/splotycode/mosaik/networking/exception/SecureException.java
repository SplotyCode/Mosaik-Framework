package io.github.splotycode.mosaik.networking.exception;

public class SecureException extends RuntimeException {

    public SecureException() {
    }

    public SecureException(String s) {
        super(s);
    }

    public SecureException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SecureException(Throwable throwable) {
        super(throwable);
    }

    public SecureException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
