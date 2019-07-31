package io.github.splotycode.mosaik.util.io;

import lombok.Getter;

public class ByteArrayInputStream extends java.io.ByteArrayInputStream {

    @Getter private boolean isOriginal = true;
    @Getter private byte[] original;

    public ByteArrayInputStream(byte[] bytes) {
        super(bytes);
        original = bytes;
    }

    public ByteArrayInputStream(byte[] bytes, int i, int i1) {
        super(bytes, i, i1);
        original = bytes;
    }

    /**
     * @deprecated use {@link io.github.splotycode.mosaik.util.io.ByteArrayInputStream#getOriginal()}
     **/
    @Deprecated
    public byte[] getBuf() {
        return buf;
    }

    @Override
    public synchronized int read(byte[] bytes, int i, int i1) {
        isOriginal = false;
        return super.read(bytes, i, i1);
    }
}
