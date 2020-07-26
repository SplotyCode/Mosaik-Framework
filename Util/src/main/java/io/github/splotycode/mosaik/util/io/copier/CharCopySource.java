package io.github.splotycode.mosaik.util.io.copier;

import java.io.IOException;

public interface CharCopySource<S> extends CopySource<S, char[]> {
    default <D> D copyTo(S source, CharCopyDestination<D> copier,
                         char[] buffer, int initialSize) throws IOException {
        return BufferedCopier.INSTANCE.copy(source, this, copier, buffer, initialSize);
    }

    default <D> long copyTo(S source, D destination, CharCopyDestination<D> destinationType,
                            char[] buffer) throws IOException {
        return BufferedCopier.INSTANCE.copy(source, this, destination, destinationType, buffer);
    }
}
