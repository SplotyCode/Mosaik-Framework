package io.github.splotycode.mosaik.webapi.response.content.manipulate.pattern;

public class PatternNotFoundException extends RuntimeException {

    public PatternNotFoundException() {
    }

    public PatternNotFoundException(String s) {
        super(s);
    }

    public PatternNotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public PatternNotFoundException(Throwable throwable) {
        super(throwable);
    }

    public PatternNotFoundException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
