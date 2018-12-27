package me.david.davidlib.storage;

import me.david.davidlib.datafactory.DataFactory;
import me.david.davidlib.datafactory.DataKey;

import java.util.Collection;

public interface Document {

    <T> T getMetaData(DataKey<T> key);
    DataFactory getMetaData();

    Collection<Node> getNodes();

    Node getNode(String name);

}
