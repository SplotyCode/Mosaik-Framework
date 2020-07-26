package io.github.splotycode.mosaik.util.io.copier;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CopySources {
    public static final ByteCopySource<InputStream> STREAM = InputStream::read;

    public static final ByteCopySource<ByteBuffer> BYTE_BUFFER = (source, buffer) -> {
        int length = Math.min(buffer.length, source.remaining());
        source.get(buffer);
        return length;
    };

    public static final CharCopySource<Reader> READER = Reader::read;
}
