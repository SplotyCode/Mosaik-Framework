package io.github.splotycode.mosaik.domparsingimpl.formats.json;

import io.github.splotycode.mosaik.domparsing.dom.Document;
import io.github.splotycode.mosaik.domparsing.parsing.input.DomStringInput;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonParserTest {

    @Test
    void testJson() {
        Document document = new JsonParser()
                .parse(new DomStringInput("{\"name\": \"value\", \"name2\": \"value3\" }"));
        assertEquals(2, document.getNodes().size());
        assertEquals("value", document.getString("name"));
        assertEquals("value3", document.getString("name2"));
    }

}
