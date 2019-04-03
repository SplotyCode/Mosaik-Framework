package io.github.splotycode.mosaik.util.cache;

import io.github.splotycode.mosaik.util.cache.complex.resolver.CacheValueResolver;
import io.github.splotycode.mosaik.util.cache.complex.validator.TimeValidator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DefaultCaches {

    public static <T> Cache<T> getNormalValueResolverCache(CacheValueResolver<T> valueResolver) {
        return getNormalValueResolverBuilder(valueResolver).build();
    }

    public static <T> Cache<T> getTimeCache(CacheValueResolver<T> valueResolver, long maxAge) {
        return getNormalValueResolverBuilder(valueResolver).setValidator(new TimeValidator<>(maxAge)).build();
    }

    public static <T> CacheBuilder.NormalBuilder<T> getNormalValueResolverBuilder(CacheValueResolver<T> valueResolver) {
        return new CacheBuilder<T>().normal().setResolver(valueResolver);
    }

}
