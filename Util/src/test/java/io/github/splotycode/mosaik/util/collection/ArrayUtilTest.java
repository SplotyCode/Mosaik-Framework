package io.github.splotycode.mosaik.util.collection;

import org.junit.Test;

import static io.github.splotycode.mosaik.util.collection.ArrayUtil.insert;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class ArrayUtilTest {

    public static final int[] EXPECTED = ArrayUtil.collectInt(1, 2, 3, 4);
    public static final int[] UNFINISHED = ArrayUtil.collectInt(1, 3, 4);

    @Test
    public void insertAt() {
        assertArrayEquals(EXPECTED, insert(UNFINISHED,1, 2));
    }

}
