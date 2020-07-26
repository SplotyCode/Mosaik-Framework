package io.github.splotycode.mosaik.valuetransformer.common.primaryarray;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.collection.ArrayUtil;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;

public class IntToObjectArray extends ValueTransformer<int[], Integer[]> {

    @Override
    public Integer[] transform(int[] input, Class<? extends Integer[]> result, DataFactory info) throws Exception {
        return ArrayUtil.toObject(input);
    }
}
