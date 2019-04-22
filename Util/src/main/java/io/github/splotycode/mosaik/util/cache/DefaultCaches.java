package io.github.splotycode.mosaik.util.cache;

import io.github.splotycode.mosaik.util.cache.complex.ComplexCache;
import io.github.splotycode.mosaik.util.cache.complex.resolver.CacheValueResolver;
import io.github.splotycode.mosaik.util.cache.complex.validator.TimeValidator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Useful Caches so you dont use the Builder for a few standard caches
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DefaultCaches {

    /**
     * Returns a ComplexCache without validation
     */
    public static <T> ComplexCache<T> getNormalValueResolverCache(CacheValueResolver<T> valueResolver) {
        return getNormalValueResolverBuilder(valueResolver).build();
    }

    /**
     * Returns a ComplexCache with time validation
     */
    public static <T> ComplexCache<T> getTimeCache(CacheValueResolver<T> valueResolver, long maxAge) {
        return getNormalValueResolverBuilder(valueResolver).setValidator(new TimeValidator<>(maxAge)).build();
    }

    /**
     * Returns a ComplexCache builder without validation
     */
    public static <T> CacheBuilder.NormalBuilder<T> getNormalValueResolverBuilder(CacheValueResolver<T> valueResolver) {
        return new CacheBuilder<T>().normal().setResolver(valueResolver);
    }

}
