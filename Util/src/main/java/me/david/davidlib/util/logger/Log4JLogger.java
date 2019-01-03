package me.david.davidlib.util.logger;

import lombok.AllArgsConstructor;
import me.david.davidlib.utils.StringUtil;
import org.apache.log4j.Level;

@AllArgsConstructor
public abstract class Log4JLogger extends Logger {

    protected final org.apache.log4j.Logger logger4J;

    @Override
    public boolean isDebugEnabled() {
        return logger4J.isDebugEnabled();
    }

    @Override
    public void debug(String message) {
        logger4J.debug(message);
    }

    @Override
    public void debug(Throwable t) {
        logger4J.debug("", t);
    }

    @Override
    public void debug(String message, Throwable t) {
        logger4J.debug(message, t);
    }

    @Override
    public boolean isTraceEnabled() {
        return logger4J.isTraceEnabled();
    }

    @Override
    public void trace(String message) {
        logger4J.trace(message);
    }

    @Override
    public void trace(Throwable t) {
        logger4J.trace("", t);
    }

    @Override
    public void info(String message) {
        logger4J.info(message);
    }

    @Override
    public void info(String message, Throwable t) {
        logger4J.info(message, t);
    }

    @Override
    public void warn(String message, Throwable t) {
        logger4J.warn(message, t);
    }

    @Override
    public void error(String message, Throwable t, String... details) {
        String fullMessage = details.length > 0 ? message + "\nDetails: " + StringUtil.join(details, "\n") : message;
        logger4J.error(fullMessage, t);
    }

    @Override
    public void setLevel(Level level) {
        logger4J.setLevel(level);
    }

}
