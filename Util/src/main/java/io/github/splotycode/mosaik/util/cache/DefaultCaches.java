package io.github.splotycode.mosaik.util.cache;

import io.github.splotycode.mosaik.util.cache.complex.resolver.CacheValueResolver;

public final class DefaultCaches {

    public static <T> Cache<T> getNormalValueResolverCache(CacheValueResolver<T> valueResolver) {
        return new CacheBuilder<T>().normal().setResolver(valueResolver).build();
    }

}
