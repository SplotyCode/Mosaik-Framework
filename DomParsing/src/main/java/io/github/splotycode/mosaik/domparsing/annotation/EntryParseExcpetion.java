package io.github.splotycode.mosaik.domparsing.annotation;

public class EntryParseExcpetion extends RuntimeException {

    public EntryParseExcpetion() {
    }

    public EntryParseExcpetion(String s) {
        super(s);
    }

    public EntryParseExcpetion(String s, Throwable throwable) {
        super(s, throwable);
    }

    public EntryParseExcpetion(Throwable throwable) {
        super(throwable);
    }

    public EntryParseExcpetion(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
