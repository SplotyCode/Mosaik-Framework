package io.github.splotycode.mosaik.runtime.logging;

import io.github.splotycode.mosaik.util.logger.Logger;
import io.github.splotycode.mosaik.util.logger.LoggerFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class CurrentLoggerHolder {
    private final Class loggingClass;
    private Logger logger;
    private LoggerFactory lastFactory;

    public Optional<Logger> getLogger() {
        LoggerFactory currentFactory = Logger.getFactory();
        if (currentFactory == null) {
            return Optional.empty();
        }
        if (lastFactory != currentFactory) {
            lastFactory = currentFactory;
            logger = Logger.getInstance(loggingClass);
        }
        return Optional.of(logger);
    }
}
