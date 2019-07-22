package io.github.splotycode.mosaik.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class StringUtilTest {

    @Test
    public void isEmpty() {
        assertTrue(StringUtil.isEmpty(""));
        assertTrue(StringUtil.isEmpty(null));
        assertFalse(StringUtil.isEmpty("heyy"));
        assertFalse(StringUtil.isEmpty(" "));
    }

    @Test
    public void isEmptyDeep() {
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
    public void humanReadableBytes() {
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
    public void checkIsTypes() {
        checkIsType(StringUtil::isInteger, false);
        checkIsType(StringUtil::isLong, false);
        checkIsType(StringUtil::isFloat, true);
        checkIsType(StringUtil::isDouble, true);
    }

    @Test
    public void join() {
        String[] array = new String[] {"hello", "join", "test"};
        assertEquals("hello, join, test", StringUtil.join(array));
        assertEquals("hello,join,test", StringUtil.join(array, ","));
        assertEquals("hello, join, test", StringUtil.join(Arrays.asList(array)));
        assertEquals("hello,join,test", StringUtil.join(Arrays.asList(array), ","));
    }

    @Test
    public void charsEqualIgnoreCase() {
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
    public void endsWithChar() {
        assertFalse(StringUtil.endsWithChar(null, 'a'));
        assertFalse(StringUtil.endsWithChar("", 'a'));
        assertTrue(StringUtil.endsWithChar("hallo", 'o'));
        assertFalse(StringUtil.endsWithChar("hallo", 'b'));
        assertFalse(StringUtil.endsWithChar("hallo", 'O'));
        assertFalse(StringUtil.endsWithChar("hallO", 'o'));
    }

    @Test
    public void startsWithIgnoreCase() {
        assertFalse(StringUtil.startsWithIgnoreCase(null, "a"));
        assertFalse(StringUtil.startsWithIgnoreCase("", "a"));
        assertTrue(StringUtil.startsWithIgnoreCase("hallo", "h"));
        assertTrue(StringUtil.startsWithIgnoreCase("hallo", "H"));
        assertTrue(StringUtil.startsWithIgnoreCase("Hallo", "h"));
        assertFalse(StringUtil.startsWithIgnoreCase("Dallo", "B"));
    }

    @Test
    public void camelCase() {
        assertEquals("Hallo My Name Is David", StringUtil.camelCase("hallo my name is david"));
        assertEquals("Hallo-My-Name-Is-David", StringUtil.camelCase("hallo-my-name-is-david", "-"));
    }

    @Test
    public void getLastSplit() {
        assertEquals("is", StringUtil.getLastSplit("hallo here it is", " "));
        assertNull(StringUtil.getLastSplit(null, " "));
        assertNull(StringUtil.getLastSplit("hallo here it is", null));
    }

    @Test
    public void endsWithIgnoreCase() {
        assertTrue(StringUtil.endsWithIgnoreCase("asdasbcaasdaabc", "abc"));
        assertFalse(StringUtil.endsWithIgnoreCase("asdasbcaasdaabcc", "abc"));

        assertTrue(StringUtil.endsWithIgnoreCase("abc", "abc"));
        assertFalse(StringUtil.endsWithIgnoreCase("ab", "abc"));

        assertFalse(StringUtil.endsWithIgnoreCase(null, null));
    }

    @Test
    public void containsIgnoreCase() {
        assertFalse(StringUtil.containsIgnoreCase("adsasdasd", "hallo"));
        assertTrue(StringUtil.containsIgnoreCase("hallo", "hallo"));
        assertTrue(StringUtil.containsIgnoreCase("HALLO", "hallo"));
        assertTrue(StringUtil.containsIgnoreCase("aHALLOa", "hallo"));
        assertTrue(StringUtil.containsIgnoreCase("aHalLOa", "hALLO"));
    }

    @Test
    public void repeat() {
        assertEquals("hahahaha", StringUtil.repeat("ha", 4));
        assertEquals("jjjjj", StringUtil.repeat("j", 5));
        assertEquals("", StringUtil.repeat("hallo", 0));

        assertNull(StringUtil.repeat(null, 4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void repeatNegative() {
        StringUtil.repeat("jjjj", -1);
    }

    @Test
    public void removeLast() {
        assertEquals("hallo", StringUtil.removeLast("hallo1234", 4));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void removeLastIndex() {
        assertEquals("hallo", StringUtil.removeLast("1", 2));
    }

    @Test
    public void removeEnd() {
        assertEquals("str", StringUtil.removeEnd("str", "hey"));
        assertEquals("hallo", StringUtil.removeEnd("hallo12", "12"));

        assertEquals("hallo", testRemoveEnd("hallo12", "12", true));

        assertEquals("hallo", testRemoveEnd("hallo12", "22", true));
        assertEquals("hallo12", testRemoveEnd("hallo12", "22", false));
    }

    private String testRemoveEnd(String str, String end, boolean onlyPossible) {
        StringBuilder builder = new StringBuilder(str);
        StringUtil.removeEnd(builder, end, onlyPossible);
        return builder.toString();
    }

    @Test
    public void getLast() {
        assertEquals("", StringUtil.getLast("hallo", 0));
        assertEquals("", StringUtil.getLast("hallo", -2));
        assertEquals("", StringUtil.getLast(null, 2));
        assertEquals("hallo", StringUtil.getLast("hallo", 100));
        assertEquals("lo", StringUtil.getLast("hallo", 2));
    }
}
