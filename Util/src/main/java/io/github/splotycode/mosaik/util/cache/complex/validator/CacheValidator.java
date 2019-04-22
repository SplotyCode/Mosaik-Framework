package io.github.splotycode.mosaik.util.cache.complex.validator;

import io.github.splotycode.mosaik.util.cache.Cache;
import io.github.splotycode.mosaik.util.cache.CacheListener;

/**
 * Validates a cache value
 */
public interface CacheValidator<T> extends CacheListener<T> {

    /**
     * Checks if a value is valid
     */
    boolean isValid(Cache<T> cache, T value);

}
