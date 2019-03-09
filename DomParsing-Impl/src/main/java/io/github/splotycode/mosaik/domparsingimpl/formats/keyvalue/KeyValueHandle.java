package io.github.splotycode.mosaik.domparsingimpl.formats.keyvalue;

import io.github.splotycode.mosaik.domparsing.parsing.DomParser;
import io.github.splotycode.mosaik.domparsing.parsing.ParsingHandle;
import io.github.splotycode.mosaik.domparsing.parsing.input.DomInput;
import io.github.splotycode.mosaik.domparsing.writing.DomWriter;
import io.github.splotycode.mosaik.domparsingimpl.formats.keyvalue.dom.KeyValueDocument;

public class KeyValueHandle implements ParsingHandle<KeyValueDocument> {

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
    public DomParser getParser(DomInput input) {
        return new KeyValueParser();
    }

    @Override
    public DomWriter getWriter() {
        return writer;
    }

}
