package io.github.splotycode.mosaik.webapi.response.content.manipulate;

public class SyntaxException extends RuntimeException {

    public SyntaxException() {
    }

    public SyntaxException(String s) {
        super(s);
    }

    public SyntaxException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SyntaxException(Throwable throwable) {
        super(throwable);
    }

    public SyntaxException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
