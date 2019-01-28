package io.github.splotycode.mosaik.webapi.response.content.manipulate;

public class ManipulationException extends RuntimeException {

    public ManipulationException() {
    }

    public ManipulationException(String s) {
        super(s);
    }

    public ManipulationException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ManipulationException(Throwable throwable) {
        super(throwable);
    }

    public ManipulationException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
