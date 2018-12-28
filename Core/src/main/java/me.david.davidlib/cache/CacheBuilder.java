package me.david.davidlib.cache;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.david.davidlib.cache.complex.ComplexCache;
import me.david.davidlib.cache.complex.resolver.CacheResolver;
import me.david.davidlib.cache.complex.validator.CacheValidator;
import me.david.davidlib.cache.complex.validator.Validators;
import me.david.davidlib.listener.ListenerHandler;

public class CacheBuilder<T>{

    public SimpleCache<T> simple() {
        return new SimpleCache<>();
    }

    public NormalBuilder<T> normal() {
        return new NormalBuilder<>();
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
            if (validator == null) validator = Validators.getALWAYS_TRUE();

            ComplexCache<T> cache = new ComplexCache<>(resolver, validator, initialValue);
            if (handler != null)
                cache.setHandler(handler);
            return cache;
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

}
