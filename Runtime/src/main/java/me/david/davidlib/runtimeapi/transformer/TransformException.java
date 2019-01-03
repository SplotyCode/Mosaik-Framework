package me.david.davidlib.runtimeapi.transformer;

public class TransformException extends RuntimeException {

    public TransformException() {
    }

    public TransformException(String s) {
        super(s);
    }

    public TransformException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public TransformException(Throwable throwable) {
        super(throwable);
    }

    public TransformException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
