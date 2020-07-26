package io.github.splotycode.mosaik.valuetransformer.common.primaryarray;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.collection.ArrayUtil;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;

public class DoubleToObjectArray extends ValueTransformer<double[], Double[]> {

    @Override
    public Double[] transform(double[] input, Class<? extends Double[]> result, DataFactory info) throws Exception {
        return ArrayUtil.toObject(input);
    }
}
