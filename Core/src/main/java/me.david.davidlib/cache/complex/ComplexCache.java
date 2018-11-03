package me.david.davidlib.cache.complex;

import lombok.Getter;
import lombok.Setter;
import me.david.davidlib.cache.Cache;
import me.david.davidlib.cache.CacheListener;
import me.david.davidlib.cache.complex.resolver.CacheResolver;
import me.david.davidlib.cache.complex.validator.CacheValidator;
import me.david.davidlib.listener.ListenerHandler;

public class ComplexCache<T> implements Cache<T> {

    @Getter private ListenerHandler<CacheListener<T>> handler = new ListenerHandler<>();

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
