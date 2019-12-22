package io.github.splotycode.mosaik.domparsingimpl.formats.keyvalue;

import io.github.splotycode.mosaik.domparsing.parsing.DefaultStringDomParser;
import io.github.splotycode.mosaik.domparsingimpl.formats.keyvalue.dom.KeyValueDocument;
import io.github.splotycode.mosaik.domparsingimpl.formats.keyvalue.readers.CommentReader;
import io.github.splotycode.mosaik.domparsingimpl.formats.keyvalue.readers.KeyReader;
import lombok.Getter;

public class KeyValueParser extends DefaultStringDomParser<KeyValueDocument, KeyValueParser> {

    @Getter private KeyValueDocument document = new KeyValueDocument();

    public KeyValueParser() {
        setReaders(new KeyReader(), new CommentReader());
    }

    @Override
    protected KeyValueDocument getResult() {
        return document;
    }
}
