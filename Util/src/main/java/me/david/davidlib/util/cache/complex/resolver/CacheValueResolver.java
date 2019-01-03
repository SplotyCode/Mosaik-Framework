package me.david.davidlib.util.cache.complex.resolver;

import me.david.davidlib.util.cache.Cache;

public interface CacheValueResolver<T> extends CacheResolver<T> {

    @Override
    default void resolve(Cache<T> cache) {
        cache.setValue(revolveValue(cache));
    }

    T revolveValue(Cache<T> cache);

}
