package me.david.davidlib.exception;

public class MethodNotSupportedExcpetion extends RuntimeException {

    public MethodNotSupportedExcpetion() {
    }

    public MethodNotSupportedExcpetion(String s) {
        super(s);
    }

    public MethodNotSupportedExcpetion(String s, Throwable throwable) {
        super(s, throwable);
    }

    public MethodNotSupportedExcpetion(Throwable throwable) {
        super(throwable);
    }

    public MethodNotSupportedExcpetion(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
