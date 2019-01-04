package me.david.davidlib.runtime.storage;

import me.david.davidlib.runtime.parsing.dom.attribute.attriute.Attribute;

import java.util.Collection;
import java.util.Map;

public interface Node {

    String name();
    Map<String, Attribute> attributes();

    Node parent();
    Collection<Node> childs();

}
