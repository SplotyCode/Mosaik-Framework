package io.github.splotycode.mosaik.valuetransformer.common.primaryarray;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.collection.ArrayUtil;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;

public class FloatToPrimitiveArray extends ValueTransformer<Float[], float[]> {

    @Override
    public float[] transform(Float[] input, Class<? extends float[]> result, DataFactory info) throws Exception {
        return ArrayUtil.toPrimitive(input);
    }
}
