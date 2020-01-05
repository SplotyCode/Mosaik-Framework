package io.github.splotycode.mosaik.domparsingimpl.formats.keyvalue;

import io.github.splotycode.mosaik.domparsing.dom.Document;
import io.github.splotycode.mosaik.domparsing.dom.value.ValueNode;
import io.github.splotycode.mosaik.domparsing.parsing.output.DomOutput;
import io.github.splotycode.mosaik.domparsing.parsing.output.DomStringOutput;
import io.github.splotycode.mosaik.domparsing.writing.DomWriter;
import io.github.splotycode.mosaik.util.collection.CollectionUtil;
import io.github.splotycode.mosaik.util.commoni.Nameable;
import io.github.splotycode.mosaik.util.node.Childable;

import java.util.Collection;

public class KeyValueWriter implements DomWriter {

    @Override
    public DomOutput write(Document document) {
        StringBuilder builder = new StringBuilder();
        for (Nameable node : document.getAllNodes(true, true)) {
            Collection<ValueNode> values = ((Childable<ValueNode>) node).getChildes();
            /* Currently no array support */

            ValueNode value = CollectionUtil.getAny(values);
            if (value.supportsToString()) {
                builder.append(node.name()).append(": ");
                builder.append(CollectionUtil.getAny(values).toString()).append("\n");
            }
        }
        return new DomStringOutput(builder.toString());
    }

}
