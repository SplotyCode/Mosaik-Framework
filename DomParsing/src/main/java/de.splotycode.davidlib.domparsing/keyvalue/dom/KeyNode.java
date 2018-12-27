package de.splotycode.davidlib.domparsing.keyvalue.dom;

import me.david.davidlib.parsing.dom.attribute.attriute.Attribute;
import me.david.davidlib.storage.Node;

import java.util.*;

public class KeyNode implements Node {

    private String name;
    private List<Node> values;

    public KeyNode(String name) {
        this(name, new ArrayList<>());
    }

    public KeyNode(String name, List<Node> values) {
        this.name = name;
        this.values = values;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Map<String, Attribute> attributes() {
        return Collections.emptyMap();
    }

    @Override
    public Node parent() {
        return null;
    }

    @Override
    public Collection<Node> childs() {
        return values;
    }
}
