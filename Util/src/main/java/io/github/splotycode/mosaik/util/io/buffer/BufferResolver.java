package io.github.splotycode.mosaik.util.io.buffer;

public interface BufferResolver {
    static BufferResolver createStatic(int size) {
        BufferHolder holder = new BufferHolder();
        return new BufferResolver() {
            @Override
            public <T> T resolveBuffer(BufferType<T> type) {
                T buffer = type.provide(holder);
                if (buffer == null) {
                    buffer = type.create(size);
                    type.update(holder, buffer);
                }
                return buffer;
            }
        };
    }

    static BufferResolver createThreadLocal(int size) {
        ThreadLocal<BufferHolder> threadLocal = ThreadLocal.withInitial(() -> new BufferHolder(size));
        return new BufferResolver() {
            @Override
            public <T> T resolveBuffer(BufferType<T> type) {
                return type.provide(threadLocal.get());
            }
        };
    }

    <T> T resolveBuffer(BufferType<T> type);
}
