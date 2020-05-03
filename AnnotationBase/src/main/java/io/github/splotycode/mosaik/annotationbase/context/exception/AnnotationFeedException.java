package io.github.splotycode.mosaik.annotationbase.context.exception;

public class AnnotationFeedException extends RuntimeException {

    public AnnotationFeedException() {
    }

    public AnnotationFeedException(String s) {
        super(s);
    }

    public AnnotationFeedException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AnnotationFeedException(Throwable throwable) {
        super(throwable);
    }

    public AnnotationFeedException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
