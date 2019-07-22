package io.github.splotycode.mosaik.util.logger;

import io.github.splotycode.mosaik.util.StringUtil;
import lombok.AllArgsConstructor;
import org.apache.log4j.Level;

@AllArgsConstructor
public class ConsoleLogger extends Logger {

    private static final boolean debug;

    static {
        String debugStr = System.getenv("mosaik-console-debug");
        debug = !StringUtil.isEmpty(debugStr) && debugStr.equalsIgnoreCase("true");
    }

    private String name;

    @Override
    public void setLevel(Level level) {}

    @Override
    public boolean isDebugEnabled() {
        return debug;
    }

    @Override
    public void debug(String message) {
        if (debug) {
            System.out.println("[DEBUG] " + name + " | " + message);
        }
    }

    @Override
    public void debug(Throwable t) {
        if (t != null) {
            t.printStackTrace();
        }
    }

    @Override
    public void debug(String message, Throwable t) {
        debug(message);
        debug(t);
    }

    @Override
    public void info(String message) {
        System.out.println("[INFO] " + name + " | " + message);
    }

    @Override
    public void info(String message, Throwable t) {
        System.out.println("[INFO] " + name + " | " + message);
        if (t != null) t.printStackTrace(System.out);
    }

    @Override
    public void warn(String message, Throwable t) {
        System.err.println("[WARN] " + name + " | " + message);
        if (t != null) t.printStackTrace(System.err);
    }

    @Override
    public void error(String message, Throwable t, String... details) {
        System.err.println("[ERROR] " + name + " | " + message);
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
