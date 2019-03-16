package io.github.splotycode.mosaik.util.reflection.annotation.exception;

public class AnnotationLoadError extends Error {

    public AnnotationLoadError() {
    }

    public AnnotationLoadError(String s) {
        super(s);
    }

    public AnnotationLoadError(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AnnotationLoadError(Throwable throwable) {
        super(throwable);
    }

    public AnnotationLoadError(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
