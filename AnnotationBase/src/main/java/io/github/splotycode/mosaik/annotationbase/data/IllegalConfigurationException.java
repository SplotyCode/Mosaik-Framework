package io.github.splotycode.mosaik.annotationbase.data;

public class IllegalConfigurationException extends RuntimeException {

    public IllegalConfigurationException() {
    }

    public IllegalConfigurationException(String message) {
        super(message);
    }

    public IllegalConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalConfigurationException(Throwable cause) {
        super(cause);
    }

    public IllegalConfigurationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
