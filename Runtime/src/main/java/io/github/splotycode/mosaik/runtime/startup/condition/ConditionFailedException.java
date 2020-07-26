package io.github.splotycode.mosaik.runtime.startup.condition;

public class ConditionFailedException extends RuntimeException {
    public ConditionFailedException() {
    }

    public ConditionFailedException(String message) {
        super(message);
    }

    public ConditionFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConditionFailedException(Throwable cause) {
        super(cause);
    }

    public ConditionFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
