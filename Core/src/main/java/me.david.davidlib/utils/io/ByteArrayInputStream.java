package me.david.davidlib.utils.io;

import lombok.Getter;

public class ByteArrayInputStream extends java.io.ByteArrayInputStream {

    @Getter private boolean original = true;

    public ByteArrayInputStream(byte[] bytes) {
        super(bytes);
    }

    public ByteArrayInputStream(byte[] bytes, int i, int i1) {
        super(bytes, i, i1);
    }

    public byte[] getBuf() {
        return buf;
    }

    @Override
    public synchronized int read(byte[] bytes, int i, int i1) {
        original = false;
        return super.read(bytes, i, i1);
    }
}
