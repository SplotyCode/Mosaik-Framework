package me.david.davidlib.runtimeapi.parsing;

public class DomParseException extends RuntimeException {

    public DomParseException() {
    }

    public DomParseException(String s) {
        super(s);
    }

    public DomParseException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public DomParseException(Throwable throwable) {
        super(throwable);
    }

    public DomParseException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
