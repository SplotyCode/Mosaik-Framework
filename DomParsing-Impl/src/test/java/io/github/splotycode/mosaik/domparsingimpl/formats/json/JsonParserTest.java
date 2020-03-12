package io.github.splotycode.mosaik.domparsingimpl.formats.json;

import io.github.splotycode.mosaik.domparsing.dom.Document;
import io.github.splotycode.mosaik.domparsing.parsing.input.DomStringInput;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JsonParserTest {

    @Test
    void testJson() {
        Document document = new JsonParser()
                .parse(new DomStringInput("{\"name\": \"value\", \"name2\": \"value3\" }"));
        assertEquals(2, document.getNodes().size());
        assertEquals("value", document.getString("name"));
        assertEquals("value3", document.getString("name2"));
    }

    @Test
    void testJsonValues() {
        Document document = new JsonParser()
                .parse(new DomStringInput("{\"name\": null, \"name2\": true }"));
        assertEquals(2, document.getNodes().size());
        assertTrue(document.isNullValue("name"));
        assertTrue(document.getBoolean("name2"));
    }

    @Test
    void testEscape() {
        Document document = new JsonParser()
                .parse(new DomStringInput("{\"name\": \"abc\\r\\n\", \"start\": \"\\rabc\", \"unicode\": \"a\\u00FCa\" }"));
        assertEquals(3, document.getNodes().size());
        assertEquals("abc\r\n", document.getString("name"));
        assertEquals("\rabc", document.getString("start"));
        assertEquals("aüa", document.getString("unicode"));
    }

    @Test
    void testJsonObjects() {
        long start = System.currentTimeMillis();
        Document document = new JsonParser()
                .parse(new DomStringInput("{\"name\": \"value\", \"obj\": {\"name2\": \"value3\"}, \"other\": \"next\" }"));
        System.out.println((System.currentTimeMillis() - start));
        assertEquals(3, document.getNodes().size());
        assertEquals("value", document.getString("name"));
        assertEquals("next", document.getString("other"));

        assertNotNull(document.getSection("obj"));
        assertEquals("value3", document.getString("obj.name2"));
    }

}
