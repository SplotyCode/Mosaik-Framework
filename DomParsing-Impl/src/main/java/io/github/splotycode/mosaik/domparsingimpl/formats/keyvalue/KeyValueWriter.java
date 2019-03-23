package io.github.splotycode.mosaik.domparsingimpl.formats.keyvalue;

import io.github.splotycode.mosaik.domparsing.dom.Document;
import io.github.splotycode.mosaik.domparsing.parsing.output.DomOutput;
import io.github.splotycode.mosaik.domparsing.parsing.output.DomStringOutput;
import io.github.splotycode.mosaik.domparsing.writing.DomWriter;
import io.github.splotycode.mosaik.util.node.NameableNode;

public class KeyValueWriter implements DomWriter {

    @Override
    public DomOutput write(Document document) {
        StringBuilder builder = new StringBuilder();
        for (NameableNode node : document.getNodes()) {
            builder.append(node.name()).append(": ");
            builder.append(document.getFirstTextFromNode(node.name())).append("\n");
        }
        return new DomStringOutput(builder.toString());
    }

}
