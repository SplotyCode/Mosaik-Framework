package io.github.splotycode.mosaik.util.io.resource;

import io.github.splotycode.mosaik.util.io.buffer.BufferProvider;
import io.github.splotycode.mosaik.util.io.buffer.BufferProviders;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class AbstractResource implements Resource {
    public static int DEFAULT_INTERNAL_BUFFER_LENGTH =
            Integer.getInteger("io.github.splotycode.mosaik.util.io.resource#DEFAULT_INTERNAL_BUFFER_LENGTH", 1024 * 512);

    /**
     * This BufferProvider should only be used internally.
     * Because using it wrongly can cause big issues elsewhere that are very hard to track down.
     * You should ensure that one thread never tries to allocate and use more then one buffer of the same type.
     */
    public static BufferProvider DEFAULT_INTERNAL_BUFFER_PROVIDER = BufferProviders.createThreadLocal(DEFAULT_INTERNAL_BUFFER_LENGTH);

    protected BufferProvider bufferProvider;

    @Override
    public BufferProvider bufferProvider() {
        return bufferProvider;
    }
}
