package io.github.splotycode.mosaik.valuetransformer.common.primaryarray;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.collection.ArrayUtil;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;

public class IntToPrimitiveArray extends ValueTransformer<Integer[], int[]> {

    @Override
    public int[] transform(Integer[] input, Class<? extends int[]> result, DataFactory info) throws Exception {
        return ArrayUtil.toPrimitive(input);
    }
}
