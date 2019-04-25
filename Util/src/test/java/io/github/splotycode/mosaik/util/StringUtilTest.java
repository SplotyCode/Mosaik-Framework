package io.github.splotycode.mosaik.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class StringUtilTest {

    @Test
    void isEmpty() {
        assertTrue(StringUtil.isEmpty(""));
        assertTrue(StringUtil.isEmpty(null));
        assertFalse(StringUtil.isEmpty("heyy"));
        assertFalse(StringUtil.isEmpty(" "));
    }

    @Test
    void isEmptyDeep() {
        assertTrue(StringUtil.isEmptyDeep(""));
        assertTrue(StringUtil.isEmptyDeep(null));
        assertTrue(StringUtil.isEmptyDeep(" "));
        assertTrue(StringUtil.isEmptyDeep("\n"));
        assertTrue(StringUtil.isEmptyDeep("\t"));
        assertTrue(StringUtil.isEmptyDeep(" \n  \r \t"));
        assertFalse(StringUtil.isEmptyDeep("a"));
        assertFalse(StringUtil.isEmptyDeep("a "));
        assertFalse(StringUtil.isEmptyDeep(" a"));
    }

    @Test
    void humanReadableBytes() {
        assertEquals("400 B", StringUtil.humanReadableBytes(400));
        assertEquals("2.0 kB", StringUtil.humanReadableBytes(2 * 1024));
        assertEquals("2.5 kB", StringUtil.humanReadableBytes(2 * 1024 + 512));
        assertEquals("1.0 MB", StringUtil.humanReadableBytes(1024 * 1024));
        assertEquals("1.0 GB", StringUtil.humanReadableBytes(1024 * 1024 * 1024));
        assertEquals("1.3 GB", StringUtil.humanReadableBytes(1024 * 1024 * 1024 + 1024 * 1024 * 265));
    }

    void checkIsType(Function<String, Boolean> function, boolean comma) {
        assertEquals(comma, function.apply("2.3f"));
        assertEquals(comma, function.apply("2.3d"));
        assertTrue(function.apply("2"));
        assertEquals(comma, function.apply("2.4"));
        assertFalse(function.apply("dd"));
        assertFalse(function.apply("2.3h"));

        assertFalse(function.apply(null));

        //Negative
        assertEquals(comma, function.apply("-2.3f"));
        assertTrue(function.apply("-2"));
        assertEquals(comma, function.apply("-2.4"));
    }

    @Test
    void checkIsTypes() {
        checkIsType(StringUtil::isInteger, false);
        checkIsType(StringUtil::isLong, false);
        checkIsType(StringUtil::isFloat, true);
        checkIsType(StringUtil::isDouble, true);
    }

    @Test
    void join() {
        String[] array = new String[] {"hello", "join", "test"};
        assertEquals("hello, join, test", StringUtil.join(array));
        assertEquals("hello,join,test", StringUtil.join(array, ","));
        assertEquals("hello, join, test", StringUtil.join(Arrays.asList(array)));
        assertEquals("hello,join,test", StringUtil.join(Arrays.asList(array), ","));
    }

    @Test
    void charsEqualIgnoreCase() {
        assertTrue(StringUtil.charsEqualIgnoreCase('a', 'a'));
        assertTrue(StringUtil.charsEqualIgnoreCase('!', '!'));

        assertTrue(StringUtil.charsEqualIgnoreCase('A', 'a'));
        assertTrue(StringUtil.charsEqualIgnoreCase('A', 'A'));
        assertTrue(StringUtil.charsEqualIgnoreCase('a', 'A'));

        assertTrue(StringUtil.charsEqualIgnoreCase('Ä', 'ä'));
        assertTrue(StringUtil.charsEqualIgnoreCase('Ä', 'Ä'));
        assertTrue(StringUtil.charsEqualIgnoreCase('ä', 'Ä'));

        assertFalse(StringUtil.charsEqualIgnoreCase('b', 'c'));
    }

    @Test
    void endsWithChar() {
        assertFalse(StringUtil.endsWithChar(null, 'a'));
        assertFalse(StringUtil.endsWithChar("", 'a'));
        assertTrue(StringUtil.endsWithChar("hallo", 'o'));
        assertFalse(StringUtil.endsWithChar("hallo", 'b'));
        assertFalse(StringUtil.endsWithChar("hallo", 'O'));
        assertFalse(StringUtil.endsWithChar("hallO", 'o'));
    }

    @Test
    void startsWithIgnoreCase() {
        assertFalse(StringUtil.startsWithIgnoreCase(null, "a"));
        assertFalse(StringUtil.startsWithIgnoreCase("", "a"));
        assertTrue(StringUtil.startsWithIgnoreCase("hallo", "h"));
        assertTrue(StringUtil.startsWithIgnoreCase("hallo", "H"));
        assertTrue(StringUtil.startsWithIgnoreCase("Hallo", "h"));
        assertFalse(StringUtil.startsWithIgnoreCase("Dallo", "B"));
    }

    @Test
    void camelCase() {
        assertEquals("Hallo My Name Is David", StringUtil.camelCase("hallo my name is david"));
        assertEquals("Hallo-My-Name-Is-David", StringUtil.camelCase("hallo-my-name-is-david", "-"));
    }

    @Test
    void getLastSplit() {
        assertEquals("is", StringUtil.getLastSplit("hallo here it is", " "));
        assertNull(StringUtil.getLastSplit(null, " "));
        assertNull(StringUtil.getLastSplit("hallo here it is", null));
    }

}
