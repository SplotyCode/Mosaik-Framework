package io.github.splotycode.mosaik.util.cache;

import io.github.splotycode.mosaik.util.cache.complex.resolver.CacheValueResolver;

public abstract class LazyLoad<T> extends SimpleCache<T> {

    public static final Object UNINITIALIZED = new Object();

    public static <T> LazyLoad<T> fromResolver(CacheValueResolver<T> resolver) {
        return new LazyLoad<T>() {
            @Override
            protected T initialize() {
                return resolver.revolveValue(this);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public LazyLoad() {
        this((T) UNINITIALIZED);
    }

    public LazyLoad(T value) {
        super(value);
    }

    protected abstract T initialize();

    @Override
    public T getValue() {
        if (value == UNINITIALIZED) {
            setValue(initialize());
        }
        return value;
    }
}