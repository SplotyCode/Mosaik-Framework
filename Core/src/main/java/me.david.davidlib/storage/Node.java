package me.david.davidlib.storage;


import me.david.davidlib.parsing.dom.attribute.attriute.Attribute;

import java.util.Collection;
import java.util.Map;

public interface Node {

    String name();
    Map<String, Attribute> attributes();

    Node parent();
    Collection<Node> childs();

}
