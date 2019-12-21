package io.github.splotycode.mosaik.util;

import org.junit.jupiter.api.Test;

import static io.github.splotycode.mosaik.util.VersionComparingUtil.compare;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class VersionComparingUtilTest {

    @Test
    public void testCompare() {
        assertEquals(compare("", ""), 0);
        assertEquals(compare("ada.12.a", "ada.12.a"), 0);

        assertEquals(compare("12-3.2", "12-3.4"), -1);
        assertEquals(compare("12-3.2.2", "12-3.2"), 1);
    }

}
