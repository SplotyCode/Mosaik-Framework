package io.github.splotycode.mosaik.util.cache.complex.resolver;

import io.github.splotycode.mosaik.util.cache.Cache;

public interface CacheResolver<T> {

    void resolve(Cache<T> cache);

}
