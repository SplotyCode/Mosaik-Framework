package io.github.splotycode.mosaik.runtime.parsing;

import io.github.splotycode.mosaik.runtime.parsing.input.DomInput;

public interface ParsingHandle {

    String[] getFileTypes();
    String[] getMimeTypes();

    DomParser getParser(DomInput input);

}
