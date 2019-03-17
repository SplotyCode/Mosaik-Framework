package io.github.splotycode.mosaik.util.reflection.annotation.exception;

public class MethodCallExcepetion extends RuntimeException {

    public MethodCallExcepetion() {
    }

    public MethodCallExcepetion(String s) {
        super(s);
    }

    public MethodCallExcepetion(String s, Throwable throwable) {
        super(s, throwable);
    }

    public MethodCallExcepetion(Throwable throwable) {
        super(throwable);
    }

    public MethodCallExcepetion(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
