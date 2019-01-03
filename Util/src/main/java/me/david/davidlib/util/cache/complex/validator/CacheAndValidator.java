package me.david.davidlib.util.cache.complex.validator;

import lombok.AllArgsConstructor;
import me.david.davidlib.util.cache.Cache;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public class CacheAndValidator<T> implements CacheValidator<T> {

    private List<CacheValidator<T>> validators;

    @SafeVarargs
    public CacheAndValidator(CacheValidator<T>... validators) {
        this.validators = Arrays.asList(validators);
    }

    @Override
    public boolean isValid(Cache<T> cache, T value) {
        return validators.stream().allMatch(validator -> validator.isValid(cache, value));
    }

    @Override public void valueChange(T value) {}

}
