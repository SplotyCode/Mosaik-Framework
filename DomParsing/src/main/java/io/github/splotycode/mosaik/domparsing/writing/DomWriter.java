package io.github.splotycode.mosaik.domparsing.writing;

import io.github.splotycode.mosaik.domparsing.dom.Document;

public interface DomWriter<D extends Document> {

    String toText(D document);

}
