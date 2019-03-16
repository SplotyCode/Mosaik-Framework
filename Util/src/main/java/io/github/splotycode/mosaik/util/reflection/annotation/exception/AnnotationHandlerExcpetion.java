package io.github.splotycode.mosaik.util.reflection.annotation.exception;

public class AnnotationHandlerExcpetion extends RuntimeException {

    public AnnotationHandlerExcpetion() {
    }

    public AnnotationHandlerExcpetion(String s) {
        super(s);
    }

    public AnnotationHandlerExcpetion(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AnnotationHandlerExcpetion(Throwable throwable) {
        super(throwable);
    }

    public AnnotationHandlerExcpetion(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
