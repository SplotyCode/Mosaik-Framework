package io.github.splotycode.mosaik.util.reflection.annotation.exception;

public class AnnotationDataException extends RuntimeException {

    public AnnotationDataException() {
    }

    public AnnotationDataException(String s) {
        super(s);
    }

    public AnnotationDataException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AnnotationDataException(Throwable throwable) {
        super(throwable);
    }

    public AnnotationDataException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
