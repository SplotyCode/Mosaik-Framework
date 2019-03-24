package io.github.splotycode.mosaik.netty.exception;

public class OutOfPortsException extends RuntimeException {

    public OutOfPortsException() {
    }

    public OutOfPortsException(String s) {
        super(s);
    }

    public OutOfPortsException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public OutOfPortsException(Throwable throwable) {
        super(throwable);
    }

    public OutOfPortsException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
