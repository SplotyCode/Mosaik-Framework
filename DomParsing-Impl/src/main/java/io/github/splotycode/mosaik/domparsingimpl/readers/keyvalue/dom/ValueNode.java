package io.github.splotycode.mosaik.domparsingimpl.readers.keyvalue.dom;

import io.github.splotycode.mosaik.domparsing.dom.attribute.Attribute;
import io.github.splotycode.mosaik.domparsing.dom.Node;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class ValueNode implements Node {

    private KeyNode parent;
    private String value;

    public ValueNode(KeyNode parent, String value) {
        this.parent = parent;
        this.value = value;
    }

    @Override
    public String name() {
        return value;
    }

    @Override
    public Map<String, Attribute> attributes() {
        return Collections.EMPTY_MAP;
    }

    @Override
    public Node parent() {
        return parent;
    }

    @Override
    public Collection<Node> childs() {
        return Collections.EMPTY_SET;
    }
}
