package io.github.splotycode.mosaik.domparsing.dom;

import io.github.splotycode.mosaik.domparsing.parsing.ParsingHandle;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.datafactory.DataKey;
import io.github.splotycode.mosaik.util.node.NameableNode;

import java.util.Collection;

public interface Document {

    ParsingHandle getHandle();

    <T> T getMetaData(DataKey<T> key);
    DataFactory getMetaData();

    Collection<NameableNode> getNodes();

    NameableNode getNode(String name);
    String getFirstTextFromNode(String name);

    void addNode(NameableNode node);

    void addNodeWithInnerText(NameableNode key, TextNode text);

    void addNodeWithInnerText(String key, String text);

}
