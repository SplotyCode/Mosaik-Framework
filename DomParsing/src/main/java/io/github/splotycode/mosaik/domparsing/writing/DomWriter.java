package io.github.splotycode.mosaik.domparsing.writing;

import io.github.splotycode.mosaik.domparsing.dom.Document;
import io.github.splotycode.mosaik.domparsing.parsing.output.DomOutput;

public interface DomWriter {

    default String toText(Document document) {
        return write(document).getString();
    }

    DomOutput write(Document document);

}
