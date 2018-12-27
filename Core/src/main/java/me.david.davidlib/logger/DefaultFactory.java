package me.david.davidlib.logger;

public class DefaultFactory implements LoggerFactory {

    @Override
    public Logger getLoggerInstance(String name) {
        return new ConsoleLogger(name);
    }
}
