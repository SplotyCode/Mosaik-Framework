package io.github.splotycode.mosaik.annotationbase.context.exception;

public class ParameterResolveException extends RuntimeException {

    public ParameterResolveException() {
    }

    public ParameterResolveException(String s) {
        super(s);
    }

    public ParameterResolveException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ParameterResolveException(Throwable throwable) {
        super(throwable);
    }

    public ParameterResolveException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
