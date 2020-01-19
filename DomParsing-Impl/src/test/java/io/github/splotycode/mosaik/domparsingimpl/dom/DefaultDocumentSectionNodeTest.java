package io.github.splotycode.mosaik.domparsingimpl.dom;

import io.github.splotycode.mosaik.domparsing.dom.Document;
import io.github.splotycode.mosaik.domparsing.parsing.input.DomStringInput;
import io.github.splotycode.mosaik.domparsingimpl.formats.keyvalue.KeyValueParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DefaultDocumentSectionNodeTest {

    @Test
    void test() {
        Document document = new KeyValueParser()
                .parse(new DomStringInput("number: 222\ndate: 22/2/2020\n"));
        assertEquals("222", document.getString("number"));
        assertEquals("22/2/2020", document.getString("date"));
        assertNull(document.getValue("abc"));
    }

}