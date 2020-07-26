package io.github.splotycode.mosaik.util.io.resource;

import io.github.splotycode.mosaik.util.io.buffer.BufferProvider;

public interface Resource {
    NativeFormat nativeFormat();

    BufferProvider bufferProvider();

    boolean writable();
    boolean readable();
}
