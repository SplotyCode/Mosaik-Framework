package io.github.splotycode.mosaik.annotationbase.context.exception;

public class InvalidAnnotationDataException extends RuntimeException {

    public InvalidAnnotationDataException() {
    }

    public InvalidAnnotationDataException(String s) {
        super(s);
    }

    public InvalidAnnotationDataException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public InvalidAnnotationDataException(Throwable throwable) {
        super(throwable);
    }

    public InvalidAnnotationDataException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
