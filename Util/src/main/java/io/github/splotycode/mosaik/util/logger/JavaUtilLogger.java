package io.github.splotycode.mosaik.util.logger;

import io.github.splotycode.mosaik.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.log4j.Level;

@AllArgsConstructor
@Getter
public class JavaUtilLogger extends Logger {

    private java.util.logging.Logger logger;

    @Override
    public void setLevel(Level level) {
        if (level == Level.ALL) {
            logger.setLevel(java.util.logging.Level.ALL);
        } else if (level == Level.OFF) {
            logger.setLevel(java.util.logging.Level.OFF);
        } else if (level == Level.INFO ) {
            logger.setLevel(java.util.logging.Level.INFO);
        } else if (level == Level.WARN) {
            logger.setLevel(java.util.logging.Level.WARNING);
        } else if (level == Level.DEBUG) {
            logger.setLevel(java.util.logging.Level.FINE);
        } else if (level == Level.ERROR || level == Level.FATAL) {
            logger.setLevel(java.util.logging.Level.SEVERE);
        } else if (level == Level.TRACE) {
            logger.setLevel(java.util.logging.Level.FINEST);
        }
        throw new IllegalStateException("Unknown log4j logging level " + level.toString());
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.getLevel().intValue() <= 500; /* 500 = Level.FINE */
    }

    @Override
    public void debug(String message) {
        logger.log(java.util.logging.Level.FINE, message);
    }

    @Override
    public void debug(Throwable t) {
        debug("", t);
    }

    @Override
    public void debug(String message, Throwable t) {
        logger.log(java.util.logging.Level.FINE, t, () -> message);
    }

    @Override
    public void info(String message) {
        logger.log(java.util.logging.Level.INFO, message);
    }

    @Override
    public void info(String message, Throwable t) {
        logger.log(java.util.logging.Level.INFO, t, () -> message);
    }

    @Override
    public void warn(String message, Throwable t) {
        logger.log(java.util.logging.Level.INFO, t, () -> message);
    }

    @Override
    public void error(String message, Throwable t, String... details) {
        String fullMessage = details.length > 0 ? message + "\nDetails: " + StringUtil.join(details, "\n") : message;
        logger.log(java.util.logging.Level.SEVERE, fullMessage);
    }
}
