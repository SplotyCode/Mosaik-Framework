package io.github.splotycode.mosaik.util.io.copier;

import io.github.splotycode.mosaik.util.io.IOUtil;

import java.io.IOException;
import java.nio.ByteBuffer;

public abstract class ByteBufferDestination implements ByteCopyDestination<ByteBuffer> {
    private static ByteBufferDestination BYTEBUFFER_DIRECT = create0(true);
    private static ByteBufferDestination BYTEBUFFER_HEAP = create0(false);

    static ByteBufferDestination EMPTY = new ByteBufferDestination() {
        @Override
        public ByteBuffer create(int initialSize) throws IOException {
            throw new IllegalStateException("Destination is empty");
        }
    };

    static ByteBufferDestination create(boolean direct) {
        return direct ? BYTEBUFFER_DIRECT : BYTEBUFFER_HEAP;
    }

    private static ByteBufferDestination create0(boolean direct) {
        return new ByteBufferDestination() {
            @Override
            public ByteBuffer create(int initialSize) {
                return IOUtil.allocateBytebuf(direct, initialSize);
            }
        };
    }

    @Override
    public void copyTo(byte[] source, ByteBuffer destination, int length) {
        destination.put(source, 0, length);
    }
}
