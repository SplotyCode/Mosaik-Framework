package io.github.splotycode.mosaik.webapi.handler;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UrlPatternTest {

    @Test
    void simplify() {
        assertEquals("/", UrlPattern.simplify("/"));
        assertEquals("/", UrlPattern.simplify("///"));

        assertEquals("hello/hello", UrlPattern.simplify("hello/hello"));
        assertEquals("hello/hello", UrlPattern.simplify("/hello/hello/"));
    }

    @Test
    void match() {
        assertFalse(new UrlPattern("/").match("test").isMatch());
        assertTrue(new UrlPattern("/").match("/").isMatch());

        assertFalse(new UrlPattern("home").match("/").isMatch());
        assertFalse(new UrlPattern("home").match("home/dev").isMatch());
        assertTrue(new UrlPattern("home").match("home").isMatch());
        assertTrue(new UrlPattern("home/abs").match("home/abs").isMatch());
        assertTrue(new UrlPattern("home/abs").match("/home/abs////").isMatch());
    }

    @Test
    void testOperators() {
        assertTrue(new UrlPattern("*").match("test").isMatch());

        assertFalse(new UrlPattern("**/hello").match("/").isMatch());
        assertTrue(new UrlPattern("**/hello").match("hello").isMatch());
        assertTrue(new UrlPattern("**/hello").match("abc/def/hello").isMatch());

        assertFalse(new UrlPattern("hello/**").match("/").isMatch());
        assertTrue(new UrlPattern("hello/**").match("hello").isMatch());
        assertTrue(new UrlPattern("hello/**").match("hello/abc/def").isMatch());


        assertFalse(new UrlPattern("**/hello/**").match("/").isMatch());
        assertTrue(new UrlPattern("**/hello/**").match("hello").isMatch());
        assertTrue(new UrlPattern("**/hello/**").match("abc/def/hello").isMatch());
        assertTrue(new UrlPattern("**/hello/**").match("hello/abc/def").isMatch());
        assertTrue(new UrlPattern("**/hello/**").match("abc/def/hello/adjkasld/adas").isMatch());
    }

    @Test
    void testVariables() {
        assertTrue(new UrlPattern("/").match("$name$/$age$").getVariables().isEmpty());

        Map<String, String> variables = new UrlPattern("$name$/test/$age$/list").match("daniel/test/44/list").getVariables();
        assertEquals(2, variables.size());
        assertEquals("name", variables.get("daniel"));
        assertEquals("44", variables.get("age"));

        UrlPattern.MatchResult result = new UrlPattern("hey/$name$").match("hey/");
        assertFalse(result.isMatch());
        assertTrue(result.getVariables().isEmpty());
    }
}