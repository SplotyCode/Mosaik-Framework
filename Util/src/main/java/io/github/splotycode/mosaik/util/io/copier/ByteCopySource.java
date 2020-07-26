package io.github.splotycode.mosaik.util.io.copier;

import java.io.IOException;

public interface ByteCopySource<S> extends CopySource<S, byte[]> {
    default <D> D copyTo(S source, ByteCopyDestination<D> destinationType,
                         byte[] buffer, int initialSize) throws IOException {
        return BufferedCopier.INSTANCE.copy(source, this, destinationType, buffer, initialSize);
    }

    default <D> long copyTo(S source, D destination, ByteCopyDestination<D> destinationType,
                         byte[] buffer) throws IOException {
        return BufferedCopier.INSTANCE.copy(source, this, destination, destinationType, buffer);
    }
}
