package io.github.splotycode.mosaik.domparsing.dom.value;

import io.github.splotycode.mosaik.util.node.Node;

public interface ValueNode extends Node {

    Object getRawValue();

    default boolean supportsToString() {
        return true;
    }

}
