package io.github.splotycode.mosaik.util.io.buffer;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class BufferHolder implements BufferProvider {
    private byte[] buffer;
    private char[] charBuffer;

    public BufferHolder(int size) {
        this(new byte[size], new char[size]);
    }

    public void updateBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public void updateCharBuffer(char[] charBuffer) {
        this.charBuffer = charBuffer;
    }

    @Override
    public byte[] provideBuffer() {
        return buffer;
    }

    @Override
    public char[] provideCharBuffer() {
        return charBuffer;
    }
}
