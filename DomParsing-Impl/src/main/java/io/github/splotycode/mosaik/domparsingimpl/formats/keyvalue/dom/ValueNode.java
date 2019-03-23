package io.github.splotycode.mosaik.domparsingimpl.formats.keyvalue.dom;

import io.github.splotycode.mosaik.domparsing.dom.TextNode;
import io.github.splotycode.mosaik.util.node.Parentable;
import lombok.Getter;

public class ValueNode implements TextNode, Parentable<KeyNode> {

    @Getter private KeyNode parent;
    private String value;

    public ValueNode(KeyNode parent, String value) {
        this.parent = parent;
        this.value = value;
    }

    @Override
    public String name() {
        return value;
    }

}
