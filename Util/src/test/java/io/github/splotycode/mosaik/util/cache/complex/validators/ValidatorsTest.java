package io.github.splotycode.mosaik.util.cache.complex.validators;

import io.github.splotycode.mosaik.util.ThreadUtil;
import io.github.splotycode.mosaik.util.cache.complex.validator.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidatorsTest {

    @Test
    void testValidators() {
        assertTrue(Validators.ALWAYS_TRUE.isValid(null, null));
        assertTrue(Validators.alwaysTrue().isValid(null, null));

        assertFalse(Validators.ALWAYS_FALSE.isValid(null, null));
        assertFalse(Validators.alwaysFalse().isValid(null, null));
    }

    @Test
    void testLogical() {
        assertTrue(new CacheAndValidator<>(
                Validators.alwaysTrue(),
                Validators.alwaysTrue(),
                Validators.alwaysTrue()
        ).isValid(null, null));
        assertFalse(new CacheAndValidator<>(
                Validators.alwaysTrue(),
                Validators.alwaysTrue(),
                Validators.alwaysFalse()
        ).isValid(null, null));

        assertTrue(new CacheOrValidator<>(
                Validators.alwaysTrue(),
                Validators.alwaysTrue(),
                Validators.alwaysTrue()
        ).isValid(null, null));
        assertTrue(new CacheOrValidator<>(
                Validators.alwaysTrue(),
                Validators.alwaysTrue(),
                Validators.alwaysFalse()
        ).isValid(null, null));
        assertFalse(new CacheOrValidator<>(
                Validators.alwaysFalse(),
                Validators.alwaysFalse(),
                Validators.alwaysFalse()
        ).isValid(null, null));
    }

    @Test
    void testTime() {
        TimeValidator<?> validator = new TimeValidator(800);
        assertTrue(validator.isValid(null, null));

        ThreadUtil.sleep(800);
        assertFalse(validator.isValid(null, null));

        validator.valueChange(null);
        assertTrue(validator.isValid(null, null));
    }

    @Test
    void testMaxedUsed() {
        MaxUsedValidator<?> validator = new MaxUsedValidator<>(5);
        for (int i = 0; i < 5; i++) {
            assertTrue(validator.isValid(null, null));
        }
        assertFalse(validator.isValid(null, null));
    }

}
