package de.splotycode.davidlib.argparser.parser;

public class ArgParseException extends RuntimeException {

    public ArgParseException() {
    }

    public ArgParseException(String s) {
        super(s);
    }

    public ArgParseException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ArgParseException(Throwable throwable) {
        super(throwable);
    }

    public ArgParseException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
