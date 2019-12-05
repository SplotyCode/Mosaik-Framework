package io.github.splotycode.mosaik.util.collection;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("WeakerAccess")
class RoundRobinTest {

    List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e", "f"));
    Iterable<String> roundRobin = new RoundRobin<>(list);

    @Test
    void testNext() {
        Iterator<String> iterator = roundRobin.iterator();
        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
        assertEquals("c", iterator.next());
        assertEquals("d", iterator.next());
        assertEquals("e", iterator.next());
        assertEquals("f", iterator.next());

        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
    }

    @Test
    void testHasNext() {
        Iterator<String> iterator = roundRobin.iterator();
        for (int i = 0; i < 10; i++) {
            assertTrue(iterator.hasNext());
        }
        assertFalse(new RoundRobin<>(Collections.emptyList()).iterator().hasNext());
    }

    @Test
    void testRemove() {
        Iterator<String> iterator = roundRobin.iterator();
        iterator.remove();
        assertFalse(list.contains("a"));

        for (int i = 0; i < 8; i++) {
            iterator.next();
        }
        assertEquals("e", iterator.next());
        iterator.remove();
        assertFalse(list.contains("e"));

        assertEquals(6 - 2, list.size());
    }

}