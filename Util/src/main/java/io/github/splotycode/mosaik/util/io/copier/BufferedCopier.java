package io.github.splotycode.mosaik.util.io.copier;

import java.io.IOException;

public class BufferedCopier implements Copier {
    public static final BufferedCopier INSTANCE = new BufferedCopier();

    @Override
    public <B, S, D> D copy(S source, CopySource<S, B> sourceType, CopyDestination<D, B> destinationType, B buffer, int initialSize) throws IOException {
        D destination = destinationType.create(initialSize);
        copy(source, sourceType, destination, destinationType, buffer);
        return destination;
    }

    @Override
    public <B, S, D> long copy(S source, CopySource<S, B> sourceType, D destination, CopyDestination<D, B> destinationType, B buffer) throws IOException {
        try {
            int total = 0;
            while (true) {
                int read = sourceType.readSource(source, buffer);
                if (read < 0) break;
                total += read;
                destinationType.copyTo(buffer, destination, read);
            }
            return total;
        } finally {
            sourceType.closeSource(source);
            destinationType.finish(destination);
        }
    }
}
