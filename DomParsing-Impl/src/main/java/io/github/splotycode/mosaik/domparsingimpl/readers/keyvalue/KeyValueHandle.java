package io.github.splotycode.mosaik.domparsingimpl.readers.keyvalue;

import io.github.splotycode.mosaik.domparsing.parsing.DomParser;
import io.github.splotycode.mosaik.domparsing.parsing.ParsingHandle;
import io.github.splotycode.mosaik.domparsing.parsing.input.DomInput;

public class KeyValueHandle implements ParsingHandle {
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
}
