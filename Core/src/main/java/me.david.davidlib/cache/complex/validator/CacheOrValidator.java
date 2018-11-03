package me.david.davidlib.cache.complex.validator;

import lombok.AllArgsConstructor;
import me.david.davidlib.cache.Cache;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public class CacheOrValidator<T> implements CacheValidator<T> {

    private List<CacheValidator<T>> validators;

    public CacheOrValidator(CacheValidator<T>... validators) {
        this.validators = Arrays.asList(validators);
    }

    @Override
    public boolean isValid(Cache<T> cache, T value) {
        return validators.stream().anyMatch(validator -> validator.isValid(cache, value));
    }
}
