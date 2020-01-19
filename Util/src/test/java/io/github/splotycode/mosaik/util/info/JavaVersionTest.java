package io.github.splotycode.mosaik.util.info;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JavaVersionTest {

    @Test
    void test() {
        JavaVersion javaVersion = JavaVersion.parse("1.8.0_181-8u181-b13-1-b13");
        assertNotNull(javaVersion);
        assertEquals(1, javaVersion.getFeature());
        assertEquals(8, javaVersion.getMinor());
    }

}