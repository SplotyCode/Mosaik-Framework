package io.github.splotycode.mosaik.database.connection.sql;

public class NoDriverException extends RuntimeException {

    public NoDriverException() {
    }

    public NoDriverException(String message) {
        super(message);
    }

    public NoDriverException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoDriverException(Throwable cause) {
        super(cause);
    }

    public NoDriverException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
