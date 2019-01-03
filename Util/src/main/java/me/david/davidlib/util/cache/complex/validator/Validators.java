package me.david.davidlib.util.cache.complex.validator;

import lombok.Getter;
import me.david.davidlib.util.cache.Cache;

public final class Validators {

    @Getter private static final CacheValidator ALWAYS_TRUE = new CacheValidator() {
        @Override
        public boolean isValid(Cache cache, Object value) {
            return true;
        }

        @Override public void valueChange(Object value) {}
    };

    @Getter private static final CacheValidator ALWAYS_FALSE = new CacheValidator() {
        @Override
        public boolean isValid(Cache cache, Object value) {
            return false;
        }

        @Override public void valueChange(Object value) {}
    };

}
