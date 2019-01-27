package io.github.splotycode.mosaik.domparsing.annotation;

import io.github.splotycode.mosaik.domparsing.dom.Document;

public interface IEntry {

    Document read();
    void write(Document document);

}
