package io.github.splotycode.mosaik.util.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HistogramTest {

    private Histogram histogram = new Histogram(0, 2, 4, 6, 8);

    private Histogram putValues(long... values) {
        for (long value : values) {
            histogram.update(value);
        }
        return histogram;
    }

    public Histogram setValues(long... values) {
        histogram.clear();
        return putValues(values);
    }

    @Test
    void min() {
        assertEquals(2, putValues(2, 2, 3).min());
        assertEquals(0, putValues(1).min());
        assertEquals(6, setValues(7).min());
    }

    @Test
    void max() {
        assertEquals(4, putValues(3, 4, 4).max());
        assertEquals(6, putValues(6).max());
    }

    @Test
    void getAverage() {
        assertEquals(2, putValues(2, 2).getAverage());
        assertEquals(0, setValues().getAverage());
        assertEquals(1.5, setValues(0, 3).getAverage());
    }

    @Test
    void clear() {
        putValues(2, 2);
        histogram.clear();
        assertEquals(0, histogram.getSize());
        assertEquals(0, histogram.getAverage());
        assertEquals(Long.MAX_VALUE, histogram.min());
        assertEquals(Long.MIN_VALUE, histogram.max());
    }

    @Test
    void getQuartile() {
        assertEquals(3, putValues(2, 2, 8, 8).getQuartile(25));
        assertEquals(8, histogram.getQuartile(75));
    }

    @Test
    void getSize() {
        assertEquals(0, histogram.getSize());
        histogram.update(5);
        histogram.update(5);
        assertEquals(2, histogram.getSize());
    }
}