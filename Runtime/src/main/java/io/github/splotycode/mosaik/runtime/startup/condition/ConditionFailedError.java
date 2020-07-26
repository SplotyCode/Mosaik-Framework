package io.github.splotycode.mosaik.runtime.startup.condition;

public class ConditionFailedError extends Error {
    public ConditionFailedError() {
    }

    public ConditionFailedError(String message) {
        super(message);
    }

    public ConditionFailedError(String message, Throwable cause) {
        super(message, cause);
    }

    public ConditionFailedError(Throwable cause) {
        super(cause);
    }

    public ConditionFailedError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
