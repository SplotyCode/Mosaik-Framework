package io.github.splotycode.mosaik.domparsing.parsing;

import io.github.splotycode.mosaik.domparsing.parsing.input.DomInput;

public interface ParsingHandle {

    String[] getFileTypes();
    String[] getMimeTypes();

    DomParser getParser(DomInput input);

}
