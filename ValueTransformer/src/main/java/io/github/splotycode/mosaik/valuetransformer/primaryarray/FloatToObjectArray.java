package io.github.splotycode.mosaik.valuetransformer.primaryarray;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.collection.ArrayUtil;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;

public class FloatToObjectArray extends ValueTransformer<float[], Float[]> {

    @Override
    public Float[] transform(float[] input, DataFactory info) throws Exception {
        return ArrayUtil.toObject(input);
    }
}
