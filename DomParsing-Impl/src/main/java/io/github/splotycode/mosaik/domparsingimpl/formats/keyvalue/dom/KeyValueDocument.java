package io.github.splotycode.mosaik.domparsingimpl.formats.keyvalue.dom;

import io.github.splotycode.mosaik.domparsing.dom.Document;
import io.github.splotycode.mosaik.domparsing.dom.TextNode;
import io.github.splotycode.mosaik.domparsing.parsing.ParsingHandle;
import io.github.splotycode.mosaik.domparsingimpl.formats.keyvalue.KeyValueHandle;
import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.runtime.Links;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.datafactory.DataKey;
import io.github.splotycode.mosaik.util.node.Childable;
import io.github.splotycode.mosaik.util.node.NameableNode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KeyValueDocument implements Document {

    @Getter private DataFactory metaData = new DataFactory();
    private List<NameableNode> nodes = new ArrayList<>();

    private static KeyValueHandle handle;

    @Override
    public ParsingHandle getHandle() {
        if (handle == null) {
            handle = LinkBase.getInstance().getLink(Links.PARSING_MANAGER).getHandleByClass(KeyValueHandle.class);
        }
        return handle;
    }

    @Override
    public <T> T getMetaData(DataKey<T> key) {
        return metaData.getData(key);
    }

    @Override
    public Collection<NameableNode> getNodes() {
        return nodes;
    }

    @Override
    public NameableNode getNode(String name) {
        return nodes.stream().filter(node -> node.name().equals(name)).findFirst().orElse(null);
    }

    @Override
    public String getFirstTextFromNode(String name) {
        NameableNode key = getNode(name);
        if (!(key instanceof Childable)) return null;
        NameableNode node = (NameableNode) ((Childable) key).getChildes().stream().filter(o -> o instanceof NameableNode).findFirst().orElse(null);
        return node == null ? null : node.name();
    }

    @Override
    public void addNode(NameableNode node) {
        nodes.add(node);
    }

    @Override
    public void addNodeWithInnerText(NameableNode key, TextNode text) {
        ((Childable) key).addChild(text);
        addNode(key);
    }

    @Override
    public void addNodeWithInnerText(String key, String text) {
        KeyNode nodeKey = new KeyNode(key);
        addNodeWithInnerText(nodeKey, new ValueNode(nodeKey, text));
    }

}
