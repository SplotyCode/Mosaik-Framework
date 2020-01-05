package io.github.splotycode.mosaik.domparsing.dom;

import io.github.splotycode.mosaik.util.datafactory.DataFactories;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.datafactory.DataKey;
import lombok.Getter;

public class DefaultDocument extends DefaultIdentifierNode implements Document {

    public static DefaultDocument createNoMetaData() {
        return new DefaultDocument(DataFactories.EMPTY_DATA_FACTORY);
    }

    @Getter protected DataFactory metaData;


    public DefaultDocument(DataFactory metaData) {
        this.metaData = metaData;
    }

    public DefaultDocument() {
        this(new DataFactory());
    }

    @Override
    public <T> T getMetaData(DataKey<T> key) {
        return metaData.getData(key);
    }

}
