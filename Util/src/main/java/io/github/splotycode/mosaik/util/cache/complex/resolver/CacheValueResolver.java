package io.github.splotycode.mosaik.util.cache.complex.resolver;

import io.github.splotycode.mosaik.util.cache.Cache;

public interface CacheValueResolver<T> extends CacheResolver<T> {

    @Override
    default void resolve(Cache<T> cache) {
        cache.setValue(revolveValue(cache));
    }

    T revolveValue(Cache<T> cache);

}
