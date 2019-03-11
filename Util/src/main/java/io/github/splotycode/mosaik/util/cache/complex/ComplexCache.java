package io.github.splotycode.mosaik.util.cache.complex;

import io.github.splotycode.mosaik.util.cache.complex.validator.CacheValidator;
import io.github.splotycode.mosaik.util.listener.DefaultListenerHandler;
import io.github.splotycode.mosaik.util.listener.ListenerHandler;
import lombok.Getter;
import lombok.Setter;
import io.github.splotycode.mosaik.util.cache.Cache;
import io.github.splotycode.mosaik.util.cache.CacheListener;
import io.github.splotycode.mosaik.util.cache.complex.resolver.CacheResolver;

public class ComplexCache<T> implements Cache<T> {

    @Setter @Getter private ListenerHandler<CacheListener<T>> handler = new DefaultListenerHandler<>();

    @Getter private CacheResolver<T> resolver;
    @Getter private CacheValidator<T> validator;

    @Setter private T value;

    public ComplexCache(CacheResolver<T> resolver, CacheValidator<T> validator) {
        this.resolver = resolver;
        this.validator = validator;
        handler.addListener(validator);
    }

    public ComplexCache(CacheResolver<T> resolver, CacheValidator<T> validator, T value) {
        this.resolver = resolver;
        this.validator = validator;
        this.value = value;
        handler.addListener(validator);
    }

    public void setValidator(CacheValidator<T> validator) {
        handler.removeListener(this.validator);
        handler.addListener(validator);
        this.validator = validator;
    }

    @Override
    public T getValue() {
        if (!validator.isValid(this, value) || value == null) {
            resolver.resolve(this);
        }
        return value;
    }

}
