package io.github.splotycode.mosaik.domparsingimpl.formats.keyvalue;

import io.github.splotycode.mosaik.domparsingimpl.dom.DefaultDocument;
import io.github.splotycode.mosaik.domparsing.parsing.DefaultStringDomParser;
import io.github.splotycode.mosaik.domparsingimpl.formats.keyvalue.readers.CommentReader;
import io.github.splotycode.mosaik.domparsingimpl.formats.keyvalue.readers.KeyReader;
import lombok.Getter;

public class KeyValueParser extends DefaultStringDomParser<DefaultDocument, KeyValueParser> {

    @Getter private DefaultDocument document = new DefaultDocument();

    public KeyValueParser() {
        setReaders(new KeyReader(), new CommentReader());
    }

    @Override
    protected DefaultDocument getResult() {
        return document;
    }
}
