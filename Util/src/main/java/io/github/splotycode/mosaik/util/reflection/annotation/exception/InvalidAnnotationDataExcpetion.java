package io.github.splotycode.mosaik.util.reflection.annotation.exception;

public class InvalidAnnotationDataExcpetion extends RuntimeException {

    public InvalidAnnotationDataExcpetion() {
    }

    public InvalidAnnotationDataExcpetion(String s) {
        super(s);
    }

    public InvalidAnnotationDataExcpetion(String s, Throwable throwable) {
        super(s, throwable);
    }

    public InvalidAnnotationDataExcpetion(Throwable throwable) {
        super(throwable);
    }

    public InvalidAnnotationDataExcpetion(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
