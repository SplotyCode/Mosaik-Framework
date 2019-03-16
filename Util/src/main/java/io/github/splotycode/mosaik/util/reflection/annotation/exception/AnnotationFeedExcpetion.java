package io.github.splotycode.mosaik.util.reflection.annotation.exception;

public class AnnotationFeedExcpetion extends RuntimeException {

    public AnnotationFeedExcpetion() {
    }

    public AnnotationFeedExcpetion(String s) {
        super(s);
    }

    public AnnotationFeedExcpetion(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AnnotationFeedExcpetion(Throwable throwable) {
        super(throwable);
    }

    public AnnotationFeedExcpetion(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
