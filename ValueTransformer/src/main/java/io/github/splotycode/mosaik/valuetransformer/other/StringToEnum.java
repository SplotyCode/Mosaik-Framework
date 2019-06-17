package io.github.splotycode.mosaik.valuetransformer.other;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.TransformerManager;

public class StringToEnum extends ValueTransformer<String, Enum> {

    @Override
    @SuppressWarnings("unchecked")
    public Enum transform(String input, DataFactory info) throws Exception {
        return Enum.valueOf(info.getData(TransformerManager.RESULT), input);
    }

}
