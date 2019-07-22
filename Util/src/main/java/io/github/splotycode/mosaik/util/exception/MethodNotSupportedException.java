package io.github.splotycode.mosaik.util.exception;

/**
 * Can be thrown if a method is not supported and should not be called in general.
 * This makes seance if the method needs to be constructed because of an interface.
 */
public class MethodNotSupportedException extends RuntimeException {

    public MethodNotSupportedException() {
    }

    public MethodNotSupportedException(String s) {
        super(s);
    }

    public MethodNotSupportedException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public MethodNotSupportedException(Throwable throwable) {
        super(throwable);
    }

    public MethodNotSupportedException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
