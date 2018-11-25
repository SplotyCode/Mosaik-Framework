package me.david.webapi.handler.anotation.parameter;

public class TransformerException extends RuntimeException {

    public TransformerException() {
    }

    public TransformerException(String s) {
        super(s);
    }

    public TransformerException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public TransformerException(Throwable throwable) {
        super(throwable);
    }

    public TransformerException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
