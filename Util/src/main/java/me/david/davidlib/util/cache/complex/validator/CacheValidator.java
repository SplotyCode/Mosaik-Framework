package me.david.davidlib.util.cache.complex.validator;

import me.david.davidlib.util.cache.Cache;
import me.david.davidlib.util.cache.CacheListener;

public interface CacheValidator<T> extends CacheListener<T> {

    boolean isValid(Cache<T> cache, T value);

}
