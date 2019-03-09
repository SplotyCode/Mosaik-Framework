package io.github.splotycode.mosaik.domparsing.parsing;

import io.github.splotycode.mosaik.domparsing.dom.Document;
import io.github.splotycode.mosaik.domparsing.parsing.input.DomInput;
import io.github.splotycode.mosaik.domparsing.writing.DomWriter;

public interface ParsingHandle<O extends Document> {

    String[] getFileTypes();
    String[] getMimeTypes();

    DomParser<O, ?> getParser(DomInput input);

    DomWriter getWriter();

}
