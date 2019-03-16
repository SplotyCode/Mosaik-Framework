package io.github.splotycode.mosaik.util.reflection.annotation.exception;

public class AnnotationDataExcpetion extends RuntimeException {

    public AnnotationDataExcpetion() {
    }

    public AnnotationDataExcpetion(String s) {
        super(s);
    }

    public AnnotationDataExcpetion(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AnnotationDataExcpetion(Throwable throwable) {
        super(throwable);
    }

    public AnnotationDataExcpetion(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
