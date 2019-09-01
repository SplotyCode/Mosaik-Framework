package io.github.splotycode.mosaik.util.cache;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LazyLoadTest {

    @Test
    void testChangedNull() {
        AtomicBoolean called = new AtomicBoolean();
        LazyLoad cache = new LazyLoad() {
            @Override
            protected Object initialize() {
                if (called.get()) {
                    throw new RuntimeException();
                }
                called.set(true);
                return null;
            }
        };
        for (int i = 0; i < 10; i++) {
            cache.getValue();
        }
    }

    @Test
    void testNeverInitialized() {
        LazyLoad cache = new LazyLoad() {
            @Override
            protected Object initialize() {
                throw new RuntimeException();
            }
        };
    }

    @Test
    void testMultipleCalls() {
        LazyLoad<Integer> cache = new LazyLoad<Integer>() {
            int counter = 0;
            @Override
            protected Integer initialize() {
                return ++counter;
            }
        };
        for (int i = 0; i < 10; i++) {
            assertEquals(1, (int) cache.getValue());
        }
    }

}
