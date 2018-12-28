package me.david.davidlib.cache;

import me.david.davidlib.cache.complex.resolver.CacheValueResolver;

public final class DefaultCaches {

    public static <T> Cache<T> getNormalValueResolverCache(CacheValueResolver<T> valueResolver) {
        return new CacheBuilder<T>().normal().setResolver(valueResolver).build();
    }

}
