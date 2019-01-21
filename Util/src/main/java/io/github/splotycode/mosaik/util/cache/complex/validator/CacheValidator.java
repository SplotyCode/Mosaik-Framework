package io.github.splotycode.mosaik.util.cache.complex.validator;

import io.github.splotycode.mosaik.util.cache.Cache;
import io.github.splotycode.mosaik.util.cache.CacheListener;

public interface CacheValidator<T> extends CacheListener<T> {

    boolean isValid(Cache<T> cache, T value);

}
