package io.github.splotycode.mosaik.util.logger;

public class JavaUtilLoggerFactory implements LoggerFactory {

    @Override
    public Logger getLoggerInstance(String name) {
        return new JavaUtilLogger(java.util.logging.Logger.getLogger(name));
    }
}
