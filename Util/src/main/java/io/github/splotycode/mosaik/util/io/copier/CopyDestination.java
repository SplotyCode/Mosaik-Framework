package io.github.splotycode.mosaik.util.io.copier;

import java.io.Closeable;
import java.io.IOException;

public interface CopyDestination<D, B> {
    D create(int initialSize) throws IOException;

    void copyTo(B source, D destination, int length) throws IOException;

    default void finish(D destination) throws IOException {
        if (destination instanceof Closeable) {
            ((Closeable) destination).close();
        }
    }
}
