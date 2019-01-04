package me.david.davidlib.runtime.startup;

public class ApplicationStartUpException extends RuntimeException {

    public ApplicationStartUpException() {
    }

    public ApplicationStartUpException(String s) {
        super(s);
    }

    public ApplicationStartUpException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ApplicationStartUpException(Throwable throwable) {
        super(throwable);
    }

    public ApplicationStartUpException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
