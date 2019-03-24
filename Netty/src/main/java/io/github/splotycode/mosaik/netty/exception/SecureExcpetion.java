package io.github.splotycode.mosaik.netty.exception;

public class SecureExcpetion extends RuntimeException {

    public SecureExcpetion() {
    }

    public SecureExcpetion(String s) {
        super(s);
    }

    public SecureExcpetion(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SecureExcpetion(Throwable throwable) {
        super(throwable);
    }

    public SecureExcpetion(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
