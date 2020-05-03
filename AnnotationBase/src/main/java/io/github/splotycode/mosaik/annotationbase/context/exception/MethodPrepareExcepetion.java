package io.github.splotycode.mosaik.annotationbase.context.exception;

public class MethodPrepareExcepetion extends RuntimeException {

    public MethodPrepareExcepetion() {
    }

    public MethodPrepareExcepetion(String s) {
        super(s);
    }

    public MethodPrepareExcepetion(String s, Throwable throwable) {
        super(s, throwable);
    }

    public MethodPrepareExcepetion(Throwable throwable) {
        super(throwable);
    }

    public MethodPrepareExcepetion(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
