package io.github.splotycode.mosaik.domparsing.dom;

import io.github.splotycode.mosaik.domparsing.parsing.ParsingHandle;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.datafactory.DataKey;

import java.util.Collection;

public interface Document {

    ParsingHandle getHandle();

    <T> T getMetaData(DataKey<T> key);
    DataFactory getMetaData();

    Collection<Node> getNodes();

    Node getNode(String name);
    String getFirstTextFromNode(String name);

    void addNode(Node node);

    void addNodeWithInnerText(Node key, TextNode text);

    void addNodeWithInnerText(String key, String text);

}
