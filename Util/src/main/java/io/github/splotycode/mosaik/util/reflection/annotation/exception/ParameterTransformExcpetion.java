package io.github.splotycode.mosaik.util.reflection.annotation.exception;

public class ParameterTransformExcpetion extends RuntimeException {

    public ParameterTransformExcpetion() {
    }

    public ParameterTransformExcpetion(String s) {
        super(s);
    }

    public ParameterTransformExcpetion(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ParameterTransformExcpetion(Throwable throwable) {
        super(throwable);
    }

    public ParameterTransformExcpetion(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
