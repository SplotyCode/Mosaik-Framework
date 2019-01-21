package io.github.splotycode.mosaik.runtime.storage;

import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.datafactory.DataKey;

import java.util.Collection;

public interface Document {

    <T> T getMetaData(DataKey<T> key);
    DataFactory getMetaData();

    Collection<Node> getNodes();

    Node getNode(String name);

}
