package io.github.splotycode.mosaik.startup.exception;

public class EnvironmentChangeExcpetion extends RuntimeException {

    public EnvironmentChangeExcpetion() {
    }

    public EnvironmentChangeExcpetion(String s) {
        super(s);
    }

    public EnvironmentChangeExcpetion(String s, Throwable throwable) {
        super(s, throwable);
    }

    public EnvironmentChangeExcpetion(Throwable throwable) {
        super(throwable);
    }

    public EnvironmentChangeExcpetion(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }

}
