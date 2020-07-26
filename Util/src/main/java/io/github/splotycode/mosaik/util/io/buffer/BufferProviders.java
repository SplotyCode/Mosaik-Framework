package io.github.splotycode.mosaik.util.io.buffer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BufferProviders {
    public static BufferProvider createStatic(int size) {
        return BufferProvider.fromResolver(BufferResolver.createStatic(size));
    }

    public static BufferProvider createThreadLocal(int size) {
        return BufferProvider.fromResolver(BufferResolver.createThreadLocal(size));
    }

    public static BufferProvider createNoCache(int size) {
        return BufferProvider.fromResolver(new BufferResolver() {
            @Override
            public <T> T resolveBuffer(BufferType<T> type) {
                return type.create(size);
            }
        });
    }
}
