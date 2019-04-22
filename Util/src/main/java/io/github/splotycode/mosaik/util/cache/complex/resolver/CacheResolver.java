package io.github.splotycode.mosaik.util.cache.complex.resolver;

import io.github.splotycode.mosaik.util.cache.Cache;

/**
 * Used to provide values for a Cache
 */
public interface CacheResolver<T> {

    /**
     * Called when the resolver needs a knew value
     */
    void resolve(Cache<T> cache);

}
