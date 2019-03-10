package io.github.splotycode.mosaik.util.cache.complex.validator;

import io.github.splotycode.mosaik.util.cache.Cache;

public final class Validators {

    public static final CacheValidator ALWAYS_TRUE = new CacheValidator() {
        @Override
        public boolean isValid(Cache cache, Object value) {
            return true;
        }

        @Override public void valueChange(Object value) {}
    };

    public static final CacheValidator ALWAYS_FALSE = new CacheValidator() {
        @Override
        public boolean isValid(Cache cache, Object value) {
            return false;
        }

        @Override public void valueChange(Object value) {}
    };

    public static <T> CacheValidator<T> alwaysTrue() {
        return ALWAYS_TRUE;
    }

    public static <T> CacheValidator<T> alwaysFalse() {
        return ALWAYS_FALSE;
    }

}
