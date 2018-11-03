package me.david.davidlib.cache.complex.resolver;

import me.david.davidlib.cache.Cache;

public interface CacheValueResolver<T> extends CacheResolver<T> {

    @Override
    default void resolve(Cache<T> cache) {
        cache.setValue(revolveValue(cache));
    }

    T revolveValue(Cache<T> cache);

}
