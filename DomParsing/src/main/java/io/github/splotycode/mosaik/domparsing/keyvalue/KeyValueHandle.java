package io.github.splotycode.mosaik.domparsing.keyvalue;

import io.github.splotycode.mosaik.runtime.parsing.DomParser;
import io.github.splotycode.mosaik.runtime.parsing.ParsingHandle;
import io.github.splotycode.mosaik.runtime.parsing.input.DomInput;

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
