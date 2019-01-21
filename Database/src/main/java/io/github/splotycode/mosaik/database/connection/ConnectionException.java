package io.github.splotycode.mosaik.database.connection;

/**
 * Throws when the Connection to a Database Fails
 */
public class ConnectionException extends RuntimeException {

    public ConnectionException() {
    }

    public ConnectionException(String s) {
        super(s);
    }

    public ConnectionException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ConnectionException(Throwable throwable) {
        super(throwable);
    }

    public ConnectionException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
