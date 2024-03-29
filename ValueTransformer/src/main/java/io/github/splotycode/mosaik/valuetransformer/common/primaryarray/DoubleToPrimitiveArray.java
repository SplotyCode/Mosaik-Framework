package io.github.splotycode.mosaik.valuetransformer.common.primaryarray;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.collection.ArrayUtil;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;

public class DoubleToPrimitiveArray extends ValueTransformer<Double[], double[]> {

    @Override
    public double[] transform(Double[] input, Class<? extends double[]> result, DataFactory info) throws Exception {
        return ArrayUtil.toPrimitive(input);
    }
}
