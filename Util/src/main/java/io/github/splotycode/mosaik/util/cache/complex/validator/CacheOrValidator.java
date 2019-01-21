package io.github.splotycode.mosaik.util.cache.complex.validator;

import lombok.AllArgsConstructor;
import io.github.splotycode.mosaik.util.cache.Cache;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public class CacheOrValidator<T> implements CacheValidator<T> {

    private List<CacheValidator<T>> validators;

    @SafeVarargs
    public CacheOrValidator(CacheValidator<T>... validators) {
        this.validators = Arrays.asList(validators);
    }

    @Override
    public boolean isValid(Cache<T> cache, T value) {
        return validators.stream().anyMatch(validator -> validator.isValid(cache, value));
    }

    @Override public void valueChange(T value) {}

}
