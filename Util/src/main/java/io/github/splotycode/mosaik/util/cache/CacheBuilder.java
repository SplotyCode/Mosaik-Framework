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

/**
 * Used to construct all kinds of Caches
 */
public class CacheBuilder<T>{

    /**
     * Generates a simple cache
     */
    public SimpleCache<T> simple() {
        return new SimpleCache<>();
    }

    /**
     * Specify that you want a normal cache.
     * E.g.: Only one value
     */
    public NormalBuilder<T> normal() {
        return new NormalBuilder<>();
    }

    /**
     * Specify that you want a map cache.
     */
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

        /**
         * Constructs the cache
         */
        public ComplexCache<T> build() {
            if (resolver == null) throw new IllegalArgumentException("Need to Specify a Resolver");
            if (validator == null) validator = Validators.alwaysTrue();

            ComplexCache<T> cache = new ComplexCache<>(resolver, validator, initialValue);
            if (handler != null)
                cache.setHandler(handler);
            return cache;
        }

        /**
         * Returns a Factory for this cache settings
         */
        public Function<?, Cache<T>> getFactory() {
            return o -> build();
        }

        /**
         * Sets the ListenerHandler for the new cache
         */
        public NormalBuilder<T> setHandler(ListenerHandler<CacheListener<T>> handler) {
            this.handler = handler;
            return this;
        }

        /**
         * Initialised value for the new cache
         */
        public NormalBuilder<T> setInitialValue(T initialValue) {
            this.initialValue = initialValue;
            return this;
        }

        /**
         * Resolver that should be used to provide values for a cache
         */
        public NormalBuilder<T> setResolver(CacheResolver<T> resolver) {
            this.resolver = resolver;
            return this;
        }

        /**
         * Validator that should be used to validate values for a cache
         */
        public NormalBuilder<T> setValidator(CacheValidator<T> validator) {
            this.validator = validator;
            return this;
        }
    }

    /**
     * Used to generate map caches
     * @param <K> key type
     * @param <V> value type
     */
    @Getter
    public static class MapBuilder<K, V> {

        private MapCache<K, V> implementation;
        private Function<K, Cache<V>> cacheFactory;
        private NormalBuilder<V> builder;
        private CacheValueResolver<V> resolver;

        /**
         * Sets the MapCache implementation.
         * Default: {@link DefaultMapCache}
         */
        public MapBuilder<K, V> setImplementation(MapCache<K, V> implementation) {
            this.implementation = implementation;
            return this;
        }

        /**
         * Resolver that should be used to provide values for a cache
         */
        public MapBuilder<K, V> setResolver(CacheValueResolver<V> resolver) {
            this.resolver = resolver;
            return this;
        }

        /**
         * Set the cache factory that should be used to construct sub caches
         */
        public MapBuilder<K, V> setCacheFactory(Function<K, Cache<V>> cacheFactory) {
            this.cacheFactory = cacheFactory;
            return this;
        }

        /**
         * Set the Sub builder that should be used to construct sub caches
         */
        public MapBuilder<K, V> setBuilder(NormalBuilder<V> builder) {
            this.builder = builder;
            return this;
        }

        /**
         * Constructs the cache
         */
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
