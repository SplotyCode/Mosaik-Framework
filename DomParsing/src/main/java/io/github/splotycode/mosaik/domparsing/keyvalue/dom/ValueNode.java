package io.github.splotycode.mosaik.domparsing.keyvalue.dom;

import io.github.splotycode.mosaik.runtime.parsing.dom.attribute.attriute.Attribute;
import io.github.splotycode.mosaik.runtime.storage.Node;

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
