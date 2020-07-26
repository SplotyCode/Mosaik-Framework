package io.github.splotycode.mosaik.util.io.copier;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class OutputStreamDestination<S extends OutputStream> implements ByteCopyDestination<S> {
    static final OutputStreamDestination EMPTY = new OutputStreamDestination() {
        @Override
        public Object create(int initialSize) throws IOException {
            throw new IllegalStateException("Destination is empty");
        }
    };

    static OutputStreamDestination<ByteArrayOutputStream> BYTE_ARRAY = new OutputStreamDestination<ByteArrayOutputStream>() {
        @Override
        public ByteArrayOutputStream create(int initialSize) {
            return new ByteArrayOutputStream(initialSize);
        }
    };

    static <S extends OutputStream> OutputStreamDestination<S> empty() {
        //noinspection unchecked
        return EMPTY;
    }

    static <O extends OutputStream> OutputStreamDestination<O> wrapStream(O stream) {
        return new OutputStreamDestination<O>() {
            @Override
            public O create(int initialSize) {
                return stream;
            }
        };
    }

    @Override
    public void copyTo(byte[] source, S destination, int length) throws IOException {
        destination.write(source, 0, length);
    }
}
