package io.github.splotycode.mosaik.networking.cloudkit;

public class ServiceNotAvailableExcpetion extends RuntimeException {

    public ServiceNotAvailableExcpetion() {
    }

    public ServiceNotAvailableExcpetion(String s) {
        super(s);
    }

    public ServiceNotAvailableExcpetion(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ServiceNotAvailableExcpetion(Throwable throwable) {
        super(throwable);
    }

    public ServiceNotAvailableExcpetion(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
