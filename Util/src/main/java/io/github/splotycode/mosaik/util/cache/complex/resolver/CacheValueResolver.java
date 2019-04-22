package io.github.splotycode.mosaik.util.cache.complex.resolver;

import io.github.splotycode.mosaik.util.cache.Cache;

/**
 * Used to provide values for a Cache
 */
public interface CacheValueResolver<T> extends CacheResolver<T> {

    @Override
    default void resolve(Cache<T> cache) {
        cache.setValue(revolveValue(cache));
    }

    /**
     * Called when the resolver needs a knew value
     * @return the knew generated value think might be null
     */
    T revolveValue(Cache<T> cache);

}
