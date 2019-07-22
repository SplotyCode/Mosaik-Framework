package io.github.splotycode.mosaik.util.reflection.annotation.exception;

public class AnnotationHandlerException extends RuntimeException {

    public AnnotationHandlerException() {
    }

    public AnnotationHandlerException(String s) {
        super(s);
    }

    public AnnotationHandlerException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AnnotationHandlerException(Throwable throwable) {
        super(throwable);
    }

    public AnnotationHandlerException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
