package io.github.splotycode.mosaik.util.reflection.classpath;

import java.io.IOException;
import java.io.UncheckedIOException;

public class ClassPathConfigurationException extends UncheckedIOException {
    public ClassPathConfigurationException(String message, IOException cause) {
        super(message, cause);
    }

    public ClassPathConfigurationException(IOException cause) {
        super(cause);
    }
}
