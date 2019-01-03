package me.david.davidlib.util.cache.complex.resolver;

import me.david.davidlib.util.cache.Cache;

public interface CacheResolver<T> {

    void resolve(Cache<T> cache);

}
