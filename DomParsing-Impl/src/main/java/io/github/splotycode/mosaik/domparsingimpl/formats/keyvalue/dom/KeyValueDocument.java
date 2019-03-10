package io.github.splotycode.mosaik.domparsingimpl.formats.keyvalue.dom;

import io.github.splotycode.mosaik.domparsing.dom.TextNode;
import io.github.splotycode.mosaik.domparsing.parsing.ParsingHandle;
import io.github.splotycode.mosaik.domparsingimpl.formats.keyvalue.KeyValueHandle;
import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.runtime.Links;
import lombok.Getter;
import io.github.splotycode.mosaik.domparsing.dom.Document;
import io.github.splotycode.mosaik.domparsing.dom.Node;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.datafactory.DataKey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KeyValueDocument implements Document {

    @Getter private DataFactory metaData = new DataFactory();
    private List<Node> nodes = new ArrayList<>();

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
    public Collection<Node> getNodes() {
        return nodes;
    }

    @Override
    public Node getNode(String name) {
        return nodes.stream().filter(node -> node.name().equals(name)).findFirst().orElse(null);
    }

    @Override
    public String getFirstTextFromNode(String name) {
        Node key = getNode(name);
        if (key == null) return null;
        return key.childs().iterator().next().name();
    }

    @Override
    public void addNode(Node node) {
        nodes.add(node);
    }

    @Override
    public void addNodeWithInnerText(Node key, TextNode text) {
        key.addChild(text);
        addNode(key);
    }

    @Override
    public void addNodeWithInnerText(String key, String text) {
        KeyNode nodeKey = new KeyNode(key);
        addNodeWithInnerText(nodeKey, new ValueNode(nodeKey, text));
    }

}
