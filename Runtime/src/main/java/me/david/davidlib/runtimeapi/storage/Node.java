package me.david.davidlib.runtimeapi.storage;

import java.util.Collection;

public interface Node {

    String name();
    Map<String, Attribute> attributes();

    Node parent();
    Collection<Node> childs();

}
