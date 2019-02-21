package io.github.splotycode.mosaik.util.datafactory;

public class DataNotFoundException extends RuntimeException {

    public DataNotFoundException() {
    }

    public DataNotFoundException(String s) {
        super(s);
    }

    public DataNotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public DataNotFoundException(Throwable throwable) {
        super(throwable);
    }

    public DataNotFoundException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }

}
