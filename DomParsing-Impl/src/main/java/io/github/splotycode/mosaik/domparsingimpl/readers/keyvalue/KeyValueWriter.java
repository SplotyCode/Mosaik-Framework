package io.github.splotycode.mosaik.domparsingimpl.readers.keyvalue;

import io.github.splotycode.mosaik.domparsing.dom.Node;
import io.github.splotycode.mosaik.domparsing.writing.DomWriter;
import io.github.splotycode.mosaik.domparsingimpl.readers.keyvalue.dom.KeyValueDocument;

public class KeyValueWriter implements DomWriter<KeyValueDocument> {

    @Override
    public String toText(KeyValueDocument document) {
        StringBuilder builder = new StringBuilder();
        for (Node node : document.getNodes()) {
            builder.append(node.name()).append(": ");
            builder.append(document.getFirstTextFromNode(node.name())).append("\n");
        }
        return builder.toString();
    }

}
