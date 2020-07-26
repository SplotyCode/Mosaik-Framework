package io.github.splotycode.mosaik.util.io;

import io.github.splotycode.mosaik.util.io.buffer.BufferProvider;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WriterOutputStream extends OutputStream {
    private static byte[] SINGLE_BYTE_WRITE_ARRAY = new byte[] {0, 0, 1};

    public static Builder builder(Writer writer) {
        return new Builder(writer);
    }

    @RequiredArgsConstructor
    @Getter
    public static class Builder {
        private final Writer writer;
        private CharsetDecoder charsetDecoder;
        private boolean writeImmediately;
        private ByteBuffer decoderIn;
        private CharBuffer decoderOut;

        public Builder withBufferProvider(BufferProvider bufferProvider) {
            withDecoderOutProvider(bufferProvider);
            return withDecoderInProvider(bufferProvider);
        }

        public Builder withDecoderInProvider(BufferProvider bufferProvider) {
            return withDecoderIn(bufferProvider.newNioBuffer());
        }

        public Builder withDecoderIn(ByteBuffer decoderIn) {
            this.decoderIn = decoderIn;
            return this;
        }

        public Builder withDecoderOutProvider(BufferProvider bufferProvider) {
            return withDecoderOut(bufferProvider.newNioCharBuffer());
        }

        public Builder withDecoderOut(CharBuffer decoderOut) {
            this.decoderOut = decoderOut;
            return this;
        }

        public Builder withCharsetDecoder(CharsetDecoder charsetDecoder) {
            this.charsetDecoder = charsetDecoder;
            return this;
        }

        public Builder withWriteImmediately(boolean writeImmediately) {
            this.writeImmediately = writeImmediately;
            return this;
        }

        public Builder withCharset(Charset charset) {
            return withCharsetDecoder(charset.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPLACE)
                    .onUnmappableCharacter(CodingErrorAction.REPLACE)
                    .replaceWith("?"));
        }

        public WriterOutputStream build() {
            if (charsetDecoder == null) {
                withCharset(Charset.defaultCharset());
            }
            if (decoderIn == null) {
                throw new IllegalStateException("No Input buffer was passed");
            }
            if (decoderOut == null) {
                throw new IllegalStateException("No Output buffer was passed");
            }
            return new WriterOutputStream(writer, charsetDecoder,
                    writeImmediately, decoderIn, decoderOut);
        }
    }

    private final Writer writer;
    private final CharsetDecoder decoder;
    private final boolean writeImmediately;

    private final ByteBuffer decoderIn;
    private final CharBuffer decoderOut;

    private void flushOutput(boolean force) throws IOException {
        int position;
        if ((force || writeImmediately) && (position = decoderOut.position()) > 0) {
            writer.write(decoderOut.array(), 0, position);
            decoderOut.rewind();
        }
    }

    private void processInput(final boolean last) throws IOException {
        decoderIn.flip();
        while (true) {
             CoderResult coderResult = decoder.decode(decoderIn, decoderOut, last);
             if (coderResult.isOverflow()) {
                flushOutput(false);
             } else if (coderResult.isUnderflow()) {
                break;
             } else {
                throw new IOException("Decoder failed");
             }
        }
        decoderIn.compact();
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        while (len > 0) {
            int c = Math.min(len, decoderIn.remaining());
            decoderIn.put(b, off, c);
            processInput(false);
            len -= c;
            off += c;
        }
        flushOutput(false);
    }

    @Override
    public void close() throws IOException {
        processInput(true);
        flushOutput(true);
        writer.close();
    }

    @Override
    public void flush() throws IOException {
        flushOutput(true);
        writer.flush();
    }

    @Override
    public void write(int b) throws IOException {
        SINGLE_BYTE_WRITE_ARRAY[0] = (byte) b;
        write(SINGLE_BYTE_WRITE_ARRAY);
    }
}
