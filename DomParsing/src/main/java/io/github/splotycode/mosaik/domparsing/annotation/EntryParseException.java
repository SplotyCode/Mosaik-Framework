package io.github.splotycode.mosaik.domparsing.annotation;

public class EntryParseException extends RuntimeException {

    public EntryParseException() {
    }

    public EntryParseException(String s) {
        super(s);
    }

    public EntryParseException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public EntryParseException(Throwable throwable) {
        super(throwable);
    }

    public EntryParseException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
