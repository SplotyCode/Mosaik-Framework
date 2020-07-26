package io.github.splotycode.mosaik.util.io.buffer;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public interface BufferProvider {
    static BufferProvider fromResolver(BufferResolver byteResolver) {
        return new BufferProvider() {
            @Override
            public byte[] provideBuffer() {
                return byteResolver.resolveBuffer(BufferType.BYTE);
            }

            @Override
            public char[] provideCharBuffer() {
                return byteResolver.resolveBuffer(BufferType.CHAR);
            }
        };
    }

    default ByteBuffer newNioBuffer() {
        return ByteBuffer.wrap(provideBuffer());
    }

    default CharBuffer newNioCharBuffer() {
        return CharBuffer.wrap(provideCharBuffer());
    }

    byte[] provideBuffer();
    char[] provideCharBuffer();
}
