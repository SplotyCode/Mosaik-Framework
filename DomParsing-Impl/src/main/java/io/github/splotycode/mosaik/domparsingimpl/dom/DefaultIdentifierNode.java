package io.github.splotycode.mosaik.domparsingimpl.dom;

import io.github.splotycode.mosaik.domparsing.dom.IdentifierNode;
import io.github.splotycode.mosaik.domparsing.dom.value.ValueNode;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class DefaultIdentifierNode implements IdentifierNode {

    protected String name;
    protected List<ValueNode> valueNodes;

    public DefaultIdentifierNode(String name) {
        this(name, new ArrayList<>());
    }

    public DefaultIdentifierNode(String name, ValueNode... values) {
        this(name, Arrays.asList(values));
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Collection<ValueNode> getChildes() {
        return valueNodes;
    }
}
