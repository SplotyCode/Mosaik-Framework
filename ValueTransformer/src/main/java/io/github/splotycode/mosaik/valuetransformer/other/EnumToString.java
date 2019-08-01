package io.github.splotycode.mosaik.valuetransformer.other;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.CommonData;

public class EnumToString extends ValueTransformer<Enum, String> {

    @Override
    public String transform(Enum input, DataFactory info) throws Exception {
        if (info.getDataDefault(CommonData.SERIALIZATION, false)) {
            return Integer.toString(input.ordinal());
        }
        return input.name();
    }
}
