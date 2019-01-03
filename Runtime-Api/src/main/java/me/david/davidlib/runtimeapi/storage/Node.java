package me.david.davidlib.runtimeapi.storage;


import me.david.davidlib.util.core.parsing.dom.attribute.attriute.Attribute;

import java.util.Collection;
import java.util.Map;

public interface Node {

    String name();
    Map<String, Attribute> attributes();

    Node parent();
    Collection<Node> childs();

}
