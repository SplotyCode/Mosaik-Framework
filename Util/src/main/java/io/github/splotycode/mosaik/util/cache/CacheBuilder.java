package io.github.splotycode.mosaik.util.cache;

import io.github.splotycode.mosaik.util.cache.complex.ComplexCache;
import io.github.splotycode.mosaik.util.cache.complex.resolver.CacheResolver;
import io.github.splotycode.mosaik.util.cache.complex.resolver.CacheValueResolver;
import io.github.splotycode.mosaik.util.cache.complex.validator.CacheValidator;
import io.github.splotycode.mosaik.util.cache.complex.validator.Validators;
import io.github.splotycode.mosaik.util.cache.map.DefaultMapCache;
import io.github.splotycode.mosaik.util.cache.map.MapCache;
import io.github.splotycode.mosaik.util.listener.ListenerHandler;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.function.Function;

public class CacheBuilder<T>{

    public SimpleCache<T> simple() {
        return new SimpleCache<>();
    }

    public NormalBuilder<T> normal() {
        return new NormalBuilder<>();
    }

    public <K> MapBuilder<K, T> map() {
        return new MapBuilder<>();
    }

    @Getter
    @EqualsAndHashCode
    public static class NormalBuilder<T> {

        private T initialValue = null;
        private ListenerHandler<CacheListener<T>> handler = null;
        private CacheResolver<T> resolver;
        private CacheValidator<T> validator;

        public ComplexCache<T> build() {
            if (resolver == null) throw new IllegalArgumentException("Need to Specify a Resolver");
            if (validator == null) validator = Validators.alwaysTrue();

            ComplexCache<T> cache = new ComplexCache<>(resolver, validator, initialValue);
            if (handler != null)
                cache.setHandler(handler);
            return cache;
        }

        public Function<?, Cache<T>> getFactory() {
            return o -> build();
        }

        public NormalBuilder<T> setHandler(ListenerHandler<CacheListener<T>> handler) {
            this.handler = handler;
            return this;
        }

        public NormalBuilder<T> setInitialValue(T initialValue) {
            this.initialValue = initialValue;
            return this;
        }

        public NormalBuilder<T> setResolver(CacheResolver<T> resolver) {
            this.resolver = resolver;
            return this;
        }

        public NormalBuilder<T> setValidator(CacheValidator<T> validator) {
            this.validator = validator;
            return this;
        }
    }

    @Getter
    public static class MapBuilder<K, V> {

        private MapCache<K, V> implementation;
        private Function<K, Cache<V>> cacheFactory;
        private NormalBuilder<V> builder;
        private CacheValueResolver<V> resolver;

        public MapBuilder<K, V> setImplementation(MapCache<K, V> implementation) {
            this.implementation = implementation;
            return this;
        }

        public MapBuilder<K, V> setResolver(CacheValueResolver<V> resolver) {
            this.resolver = resolver;
            return this;
        }

        public MapBuilder<K, V> setCacheFactory(Function<K, Cache<V>> cacheFactory) {
            this.cacheFactory = cacheFactory;
            return this;
        }

        public MapBuilder<K, V> setBuilder(NormalBuilder<V> builder) {
            this.builder = builder;
            return this;
        }

        public MapCache<K, V> build() {
            if (implementation == null) implementation = new DefaultMapCache<>();
            if (cacheFactory == null) {
                if (builder == null) {
                    if (resolver == null) {
                        throw new NullPointerException("Need cacheFactory or builder or resolver");
                    }
                    builder = DefaultCaches.getNormalValueResolverBuilder(resolver);
                }
                cacheFactory = (Function<K, Cache<V>>) builder.getFactory();
            }
            implementation.setCacheFactory(cacheFactory);
            return implementation;
        }
    }

}
