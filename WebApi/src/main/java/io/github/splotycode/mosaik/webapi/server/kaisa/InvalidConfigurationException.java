package io.github.splotycode.mosaik.webapi.server.kaisa;

public class InvalidConfigurationException extends RuntimeException {

    public InvalidConfigurationException() {
    }

    public InvalidConfigurationException(String s) {
        super(s);
    }

    public InvalidConfigurationException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public InvalidConfigurationException(Throwable throwable) {
        super(throwable);
    }

    public InvalidConfigurationException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
