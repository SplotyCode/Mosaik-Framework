package io.github.splotycode.mosaik.networking.config;

public class ConfigurationExcpetion extends RuntimeException {

    public ConfigurationExcpetion() {
    }

    public ConfigurationExcpetion(String message) {
        super(message);
    }

    public ConfigurationExcpetion(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigurationExcpetion(Throwable cause) {
        super(cause);
    }

    public ConfigurationExcpetion(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
