package me.david.davidlib.util.cache;

import me.david.davidlib.util.cache.complex.resolver.CacheValueResolver;

public final class DefaultCaches {

    public static <T> Cache<T> getNormalValueResolverCache(CacheValueResolver<T> valueResolver) {
        return new CacheBuilder<T>().normal().setResolver(valueResolver).build();
    }

}
