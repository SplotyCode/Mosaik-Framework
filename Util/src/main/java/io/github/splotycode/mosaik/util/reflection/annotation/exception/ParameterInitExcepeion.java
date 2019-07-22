package io.github.splotycode.mosaik.util.reflection.annotation.exception;

public class ParameterInitExcepeion extends RuntimeException {

    public ParameterInitExcepeion() {
    }

    public ParameterInitExcepeion(String s) {
        super(s);
    }

    public ParameterInitExcepeion(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ParameterInitExcepeion(Throwable throwable) {
        super(throwable);
    }

    public ParameterInitExcepeion(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
