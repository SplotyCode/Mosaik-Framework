package me.david.davidlib.logger;

import lombok.Getter;
import me.david.davidlib.utils.ExceptionUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Level;

public abstract class Logger {

    @Getter private static LoggerFactory factory;

    public abstract void setLevel(Level level);

    public abstract boolean isDebugEnabled();

    public boolean isTraceEnabled() {
        return isDebugEnabled();
    }

    public abstract void debug(String message);
    public abstract void debug(Throwable t);
    public abstract void debug(String message, Throwable t);

    public void trace(String message) {
        debug(message);
    }
    public void trace(Throwable t) {
        debug(t);
    }

    public void debug(String message, Object... details) {
        if (isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append(message);
            for (Object detail : details) {
                sb.append(detail);
            }
            debug(sb.toString());
        }
    }

    public void info(Throwable t) {
        info(t.getMessage(), t);
    }

    public abstract void info(String message);
    public abstract void info(String message, Throwable t);

    public void warn(String message) {
        warn(message, null);
    }
    public void warn(Throwable t) {
        warn(t.getMessage(), t);
    }
    public abstract void warn(String message, Throwable t);

    public void error(String message) {
        error(message, new Throwable(message), ArrayUtils.EMPTY_STRING_ARRAY);
    }
    public void error(Object message) {
        error(String.valueOf(message));
    }

    public void error(String message, String... details) {
        error(message, new Throwable(message), details);
    }

    public void error(String message, Throwable t) {
        error(message, t, ArrayUtils.EMPTY_STRING_ARRAY);
    }

    public void error(Throwable t) {
        error(t.getMessage(), t, ArrayUtils.EMPTY_STRING_ARRAY);
    }

    public abstract void error(String message, Throwable t, String... details);

    public boolean assertTrue(boolean value, Object message) {
        if (!value) {
            String resultMessage = "Assertion failed";
            if (message != null) resultMessage += ": " + message;
            error(resultMessage, new Throwable(resultMessage));
        }
        return value;
    }

    public boolean assertTrue(boolean value) {
        return assertTrue(value, null);
    }

    public static boolean isInitialized() {
        return !(factory instanceof DefaultFactory);
    }

    public static Logger getInstance(String category) {
        return factory.getLoggerInstance(category);
    }

    public static Logger getInstance(Class cl) {
        return getInstance("#" + cl.getName());
    }

    public static void setFactory(Class<? extends LoggerFactory> factory) {
        if (isInitialized()) {
            if (factory.isInstance(Logger.factory)) {
                return;
            }
            System.out.println("Changing log factory\n" + ExceptionUtil.toString(new Throwable()));
        }

        try {
            Logger.factory = factory.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void setFactory(LoggerFactory factory) {
        if (isInitialized()) {
            System.out.println("Changing log factory\n" + ExceptionUtil.toString(new Throwable()));
        }

        Logger.factory = factory;
    }

}
