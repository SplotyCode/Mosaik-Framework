package io.github.splotycode.mosaik.util.cache.complex.validator;

import io.github.splotycode.mosaik.util.cache.Cache;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Checks if cache value is valid by checking how often is has been used
 */
public class MaxUsedValidator<T> implements CacheValidator<T> {

    private final int max;
    private AtomicInteger used = new AtomicInteger();

    public MaxUsedValidator(int max) {
        this.max = max;
    }

    @Override
    public boolean isValid(Cache<T> cache, T value) {
        return used.incrementAndGet() <= max;
    }

    @Override
    public void valueChange(T value) {
        used.set(0);
    }
}
