package io.github.splotycode.mosaik.valuetransformer.common.primaryarray;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.collection.ArrayUtil;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;

public class BooleanToObjectArray extends ValueTransformer<boolean[], Boolean[]> {

    @Override
    public Boolean[] transform(boolean[] input, Class<? extends Boolean[]> result, DataFactory info) throws Exception {
        return ArrayUtil.toObject(input);
    }
}
