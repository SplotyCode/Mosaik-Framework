package io.github.splotycode.mosaik.networking.cloudkit;

public class ServiceNotAvailableException extends RuntimeException {

    public ServiceNotAvailableException() {
    }

    public ServiceNotAvailableException(String s) {
        super(s);
    }

    public ServiceNotAvailableException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ServiceNotAvailableException(Throwable throwable) {
        super(throwable);
    }

    public ServiceNotAvailableException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
