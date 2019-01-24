package io.github.splotycode.mosaik.domparsing.annotation;

import io.github.splotycode.mosaik.domparsing.dom.Document;

public interface DomResolver {

    void read(Document document);
    void write(Document document);

}
