package me.david.davidlib.spigotlib.exception;

public class PluginLoadException extends RuntimeException {

    public PluginLoadException() {
    }

    public PluginLoadException(String s) {
        super(s);
    }

    public PluginLoadException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public PluginLoadException(Throwable throwable) {
        super(throwable);
    }

    public PluginLoadException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
