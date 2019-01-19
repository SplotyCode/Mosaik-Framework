package me.david.davidlib.util.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String s) {
        super(s);
    }

    public ResourceNotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ResourceNotFoundException(Throwable throwable) {
        super(throwable);
    }

    public ResourceNotFoundException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
