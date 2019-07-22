package io.github.splotycode.mosaik.domparsingimpl.formats.keyvalue.dom;

import io.github.splotycode.mosaik.util.node.Childable;
import io.github.splotycode.mosaik.util.node.NameableNode;
import lombok.Getter;

import java.util.List;

public class KeyNode implements NameableNode, Childable<NameableNode> {

    private String name;
    @Getter private List<NameableNode> childes;

    public KeyNode(String name) {
        this.name = name;
    }

    public KeyNode(String name, List<NameableNode> childes) {
        this.name = name;
        this.childes = childes;
    }

    @Override
    public String name() {
        return name;
    }

}
