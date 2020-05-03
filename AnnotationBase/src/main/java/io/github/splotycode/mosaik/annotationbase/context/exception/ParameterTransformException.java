package io.github.splotycode.mosaik.annotationbase.context.exception;

public class ParameterTransformException extends RuntimeException {

    public ParameterTransformException() {
    }

    public ParameterTransformException(String s) {
        super(s);
    }

    public ParameterTransformException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ParameterTransformException(Throwable throwable) {
        super(throwable);
    }

    public ParameterTransformException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
