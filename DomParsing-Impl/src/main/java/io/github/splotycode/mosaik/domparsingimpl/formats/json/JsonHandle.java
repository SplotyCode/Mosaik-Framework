package io.github.splotycode.mosaik.domparsingimpl.formats.json;

import io.github.splotycode.mosaik.domparsing.parsing.DomParser;
import io.github.splotycode.mosaik.domparsing.parsing.ParsingHandle;
import io.github.splotycode.mosaik.domparsing.parsing.input.DomInput;
import io.github.splotycode.mosaik.domparsing.writing.DomWriter;

public class JsonHandle implements ParsingHandle {

    @Override
    public String[] getFileTypes() {
        return new String[] {"json"};
    }

    @Override
    public String[] getMimeTypes() {
        return new String[] {"application/json"};
    }

    @Override
    public DomParser getParser(DomInput input) {
        return new JsonParser();
    }

    @Override
    public DomWriter getWriter() {
        throw new UnsupportedOperationException();
    }

}
