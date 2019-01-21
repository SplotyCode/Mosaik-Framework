package io.github.splotycode.mosaik.util.logger;

import org.apache.log4j.Level;

public class ConsoleLogger extends Logger {

    public ConsoleLogger(String name) {}

    @Override public void setLevel(Level level) {}

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Override public void debug(String message) {}
    @Override public void debug(Throwable t) {}
    @Override public void debug(String message, Throwable t) {}

    @Override public void info(String message) {}
    @Override public void info(String message, Throwable t) {}

    @Override
    public void warn(String message, Throwable t) {
        System.err.println("WARN: " + message);
        if (t != null) t.printStackTrace(System.err);
    }

    @Override
    public void error(String message, Throwable t, String... details) {
        System.err.println("ERROR: " + message);
        if (t != null) t.printStackTrace(System.err);
        if (details.length > 0) {
            System.err.println("details: ");
            for (String detail : details) {
                System.err.println(detail);
            }
        }

        throw new AssertionError(message, t);
    }
}
