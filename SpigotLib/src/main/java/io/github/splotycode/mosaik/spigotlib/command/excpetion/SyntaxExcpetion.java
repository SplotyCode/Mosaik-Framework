package io.github.splotycode.mosaik.spigotlib.command.excpetion;

public class SyntaxExcpetion extends RuntimeException {

    public SyntaxExcpetion() {
    }

    public SyntaxExcpetion(String s) {
        super(s);
    }

    public SyntaxExcpetion(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SyntaxExcpetion(Throwable throwable) {
        super(throwable);
    }

    public SyntaxExcpetion(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
