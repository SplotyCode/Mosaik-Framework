package io.github.splotycode.mosaik.domparsing.dom;

import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.datafactory.DataKey;

public interface Document extends DocumentSectionNode {

    <T> T getMetaData(DataKey<T> key);
    DataFactory getMetaData();

}
