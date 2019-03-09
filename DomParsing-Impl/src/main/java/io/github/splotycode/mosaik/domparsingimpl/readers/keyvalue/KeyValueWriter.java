package io.github.splotycode.mosaik.domparsingimpl.readers.keyvalue;

import io.github.splotycode.mosaik.domparsing.dom.Document;
import io.github.splotycode.mosaik.domparsing.dom.Node;
import io.github.splotycode.mosaik.domparsing.parsing.output.DomOutput;
import io.github.splotycode.mosaik.domparsing.parsing.output.DomStringOutput;
import io.github.splotycode.mosaik.domparsing.writing.DomWriter;

public class KeyValueWriter implements DomWriter {

    @Override
    public DomOutput write(Document document) {
        StringBuilder builder = new StringBuilder();
        for (Node node : document.getNodes()) {
            builder.append(node.name()).append(": ");
            builder.append(document.getFirstTextFromNode(node.name())).append("\n");
        }
        return new DomStringOutput(builder.toString());
    }

}
