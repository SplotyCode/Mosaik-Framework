package io.github.splotycode.mosaik.runtime.storage;

import io.github.splotycode.mosaik.runtime.parsing.dom.attribute.attriute.Attribute;

import java.util.Collection;
import java.util.Map;

public interface Node {

    String name();
    Map<String, Attribute> attributes();

    Node parent();
    Collection<Node> childs();

}
