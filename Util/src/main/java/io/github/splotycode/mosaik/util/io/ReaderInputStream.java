package io.github.splotycode.mosaik.util.io;

import io.github.splotycode.mosaik.util.io.buffer.BufferProvider;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ReaderInputStream extends InputStream {
    public static ReaderInputStream.Builder builder(Reader reader) {
        return new ReaderInputStream.Builder(reader);
    }

    @RequiredArgsConstructor
    @Getter
    public static class Builder {
        private final Reader reader;
        private CharsetEncoder charsetEncoder;
        private CharBuffer decoderIn;
        private ByteBuffer decoderOut;

        public Builder withBufferProvider(BufferProvider bufferProvider) {
            withDecoderOutProvider(bufferProvider);
            return withDecoderInProvider(bufferProvider);
        }

        public Builder withDecoderInProvider(BufferProvider bufferProvider) {
            return withDecoderIn(bufferProvider.newNioCharBuffer());
        }

        public Builder withDecoderIn(CharBuffer decoderIn) {
            this.decoderIn = decoderIn;
            return this;
        }

        public Builder withDecoderOutProvider(BufferProvider bufferProvider) {
            return withDecoderOut(bufferProvider.newNioBuffer());
        }

        public Builder withDecoderOut(ByteBuffer decoderOut) {
            this.decoderOut = decoderOut;
            return this;
        }

        public Builder withCharsetEncoder(CharsetEncoder charsetEncoder) {
            this.charsetEncoder = charsetEncoder;
            return this;
        }

        public Builder withCharset(Charset charset) {
            return withCharsetEncoder(charset.newEncoder()
                    .onMalformedInput(CodingErrorAction.REPLACE)
                    .onUnmappableCharacter(CodingErrorAction.REPLACE));
        }

        public ReaderInputStream build() {
            if (charsetEncoder == null) {
                withCharset(Charset.defaultCharset());
            }
            if (decoderIn == null) {
                throw new IllegalStateException("No Input buffer was passed");
            }
            if (decoderOut == null) {
                throw new IllegalStateException("No Output buffer was passed");
            }
            return new ReaderInputStream(reader, charsetEncoder,
                    decoderIn, decoderOut);
        }
    }

    private final Reader reader;
    private final CharsetEncoder encoder;

    private final CharBuffer encoderIn;
    private final ByteBuffer encoderOut;

    private CoderResult lastCoderResult;
    private boolean endOfInput;

    private void fillBuffer() throws IOException {
        if (!endOfInput && (lastCoderResult == null || lastCoderResult.isUnderflow())) {
            encoderIn.compact();
            int position = encoderIn.position();
            int c = reader.read(encoderIn.array(), position, encoderIn.remaining());
            if (c == -1) {
                endOfInput = true;
            } else {
                encoderIn.position(position+c);
            }
            encoderIn.flip();
        }
        encoderOut.compact();
        lastCoderResult = encoder.encode(encoderIn, encoderOut, endOfInput);
        encoderOut.flip();
    }
    @Override
    public int read(byte[] array, int off, int len) throws IOException {
        if (len < 0 || off < 0 || (off + len) > array.length) {
            throw new IndexOutOfBoundsException("Array Size=" + array.length +
                    ", offset=" + off + ", length=" + len);
        }
        int read = 0;
        if (len == 0) {
            return 0; // Always return 0 if len == 0
        }
        while (len > 0) {
            if (encoderOut.hasRemaining()) {
                int c = Math.min(encoderOut.remaining(), len);
                encoderOut.get(array, off, c);
                off += c;
                len -= c;
                read += c;
            } else {
                fillBuffer();
                if (endOfInput && !encoderOut.hasRemaining()) {
                    break;
                }
            }
        }
        return read == 0 && endOfInput ? -1 : read;
    }

    @Override
    public int read() throws IOException {
        for (;;) {
            if (encoderOut.hasRemaining()) {
               return encoderOut.get() & 0xFF;
            }
            fillBuffer();
            if (endOfInput && !encoderOut.hasRemaining()) {
                return -1;/* EOF */
            }
        }
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
