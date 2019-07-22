package io.github.splotycode.mosaik.domparsing.dom;

import io.github.splotycode.mosaik.domparsing.dom.attribute.Attribute;
import io.github.splotycode.mosaik.util.node.Node;

import java.util.Map;

public interface AttributeNode extends Node {

    Map<String, Attribute> attributes();

}
