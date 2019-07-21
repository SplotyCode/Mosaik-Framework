package io.github.splotycode.mosaik.valuetransformer.primaryarray;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.collection.ArrayUtil;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;

public class BooleanToPrimitiveArray extends ValueTransformer<Boolean[], boolean[]> {

    @Override
    public boolean[] transform(Boolean[] input, DataFactory info) throws Exception {
        return ArrayUtil.toPrimitive(input);
    }
}
