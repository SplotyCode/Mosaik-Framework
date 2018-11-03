package me.david.davidlib.cache.complex.resolver;

import me.david.davidlib.cache.Cache;

public interface CacheResolver<T> {

    void resolve(Cache<T> cache);

}
