package io.github.splotycode.mosaik.domparsingimpl.formats.keyvalue;

import io.github.splotycode.mosaik.domparsingimpl.dom.DefaultDocument;
import io.github.splotycode.mosaik.domparsing.parsing.DomParser;
import io.github.splotycode.mosaik.domparsing.parsing.ParsingHandle;
import io.github.splotycode.mosaik.domparsing.parsing.input.DomInput;
import io.github.splotycode.mosaik.domparsing.writing.DomWriter;

public class KeyValueHandle implements ParsingHandle<DefaultDocument> {

    private KeyValueWriter writer = new KeyValueWriter();

    @Override
    public String[] getFileTypes() {
        return new String[] {"kv"};
    }

    @Override
    public String[] getMimeTypes() {
        return new String[] {"text/kv"};
    }

    @Override
    public DomParser<DefaultDocument, KeyValueParser> getParser(DomInput input) {
        return new KeyValueParser();
    }

    @Override
    public DomWriter getWriter() {
        return writer;
    }

}
