package io.github.splotycode.mosaik.valuetransformer;

import io.github.splotycode.mosaik.valuetransformer.common.stringtoprimary.StringToInt;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransformerManagerTest {

    @Test
    void testPrimitiveCast() {
        TransformerManager transformer = new TransformerManager();
        assertEquals(42, (int) transformer.transform(42, int.class));
        assertEquals((Integer) 42, transformer.transform(42, Integer.class));
        assertEquals(42, (int) transformer.transform(new Integer(42), int.class));
        assertEquals((Integer) 42, transformer.transform(new Integer(42), Integer.class));

        transformer.register(new StringToInt());
        assertEquals(42, (int) transformer.transform("42", int.class));
        assertEquals((Integer) 42, transformer.transform("42", Integer.class));
    }

    @Test
    void testPrimitiveNull() {
        TransformerManager transformer = new TransformerManager();
        assertEquals(0, (int) transformer.transform(null, int.class));
        assertEquals((short) 0, (short) transformer.transform(null, short.class));
        assertEquals(0L, (long) transformer.transform(null, long.class));

        assertEquals(0F, (float) transformer.transform(null, float.class));
        assertEquals(0D, (double) transformer.transform(null, double.class));

        assertEquals((char) 0, (char) transformer.transform(null, char.class));
        assertEquals(false, transformer.transform(null, boolean.class));
    }

}