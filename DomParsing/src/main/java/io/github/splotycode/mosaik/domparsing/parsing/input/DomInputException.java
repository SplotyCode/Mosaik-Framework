package io.github.splotycode.mosaik.domparsing.parsing.input;

public class DomInputException extends RuntimeException {

    public DomInputException() {
    }

    public DomInputException(String s) {
        super(s);
    }

    public DomInputException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public DomInputException(Throwable throwable) {
        super(throwable);
    }

    public DomInputException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}

