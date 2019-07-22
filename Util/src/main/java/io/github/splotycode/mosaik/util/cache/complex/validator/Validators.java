package io.github.splotycode.mosaik.util.cache.complex.validator;

import io.github.splotycode.mosaik.util.cache.Cache;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Useful Cache validators
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Validators {

    /**
     * Cache value validator that always returns true
     */
    public static final CacheValidator ALWAYS_TRUE = new CacheValidator() {
        @Override
        public boolean isValid(Cache cache, Object value) {
            return true;
        }

        @Override public void valueChange(Object value) {}
    };

    /**
     * Cache value validator that always returns false
     */
    public static final CacheValidator ALWAYS_FALSE = new CacheValidator() {
        @Override
        public boolean isValid(Cache cache, Object value) {
            return false;
        }

        @Override public void valueChange(Object value) {}
    };

    /**
     * Cache value validator that always returns true
     */
    public static <T> CacheValidator<T> alwaysTrue() {
        return ALWAYS_TRUE;
    }

    /**
     * Cache value validator that always returns false
     */
    public static <T> CacheValidator<T> alwaysFalse() {
        return ALWAYS_FALSE;
    }

}
