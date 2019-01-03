package me.david.davidlib.runtimeapi.storage;

import me.david.davidlib.util.datafactory.DataFactory;
import me.david.davidlib.util.datafactory.DataKey;

import java.util.Collection;

public interface Document {

    <T> T getMetaData(DataKey<T> key);
    DataFactory getMetaData();

    Collection<Node> getNodes();

    Node getNode(String name);

}
