package io.github.splotycode.mosaik.util.io.copier;

import java.io.Closeable;
import java.io.IOException;

public interface CopySource<S, B> {
    int readSource(S source, B buffer) throws IOException;

    default void closeSource(S source) throws IOException {
        if (source instanceof Closeable) {
            ((Closeable) source).close();
        }
    }
}
