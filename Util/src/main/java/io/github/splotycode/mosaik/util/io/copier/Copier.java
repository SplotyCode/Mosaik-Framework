package io.github.splotycode.mosaik.util.io.copier;

import java.io.IOException;

public interface Copier {
    <B, S, D> D copy(S source, CopySource<S, B> sourceType,
                     CopyDestination<D, B> destinationType,
                     B buffer, int initialSize) throws IOException;

    <B, S, D> long copy(S source, CopySource<S, B> sourceType,
                        D destination, CopyDestination<D, B> destinationType,
                        B buffer) throws IOException;
}
