package io.github.splotycode.mosaik.webapi.server;

public class ServerAlreadyRunningException extends RuntimeException {

    public ServerAlreadyRunningException() {
    }

    public ServerAlreadyRunningException(String s) {
        super(s);
    }

    public ServerAlreadyRunningException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ServerAlreadyRunningException(Throwable throwable) {
        super(throwable);
    }

    public ServerAlreadyRunningException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
