package me.david.davidlib.cache.complex.validator;

import me.david.davidlib.cache.Cache;
import me.david.davidlib.cache.CacheListener;

public interface CacheValidator<T> extends CacheListener<T> {

    boolean isValid(Cache<T> cache, T value);

}
