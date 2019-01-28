package io.github.splotycode.mosaik.domparsing.parsing.output;

public class DomOutputException extends RuntimeException {

    public DomOutputException() {
    }

    public DomOutputException(String s) {
        super(s);
    }

    public DomOutputException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public DomOutputException(Throwable throwable) {
        super(throwable);
    }

    public DomOutputException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}

