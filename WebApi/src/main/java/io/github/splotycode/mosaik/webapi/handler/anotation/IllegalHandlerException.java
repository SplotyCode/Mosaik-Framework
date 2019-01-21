package io.github.splotycode.mosaik.webapi.handler.anotation;

public class IllegalHandlerException extends RuntimeException {

    public IllegalHandlerException() {
    }

    public IllegalHandlerException(String s) {
        super(s);
    }

    public IllegalHandlerException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public IllegalHandlerException(Throwable throwable) {
        super(throwable);
    }

    public IllegalHandlerException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
