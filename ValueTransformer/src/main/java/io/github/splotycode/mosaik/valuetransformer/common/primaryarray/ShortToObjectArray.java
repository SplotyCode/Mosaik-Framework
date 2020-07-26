package io.github.splotycode.mosaik.valuetransformer.common.primaryarray;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.collection.ArrayUtil;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;

public class ShortToObjectArray extends ValueTransformer<short[], Short[]> {

    @Override
    public Short[] transform(short[] input, Class<? extends Short[]> result, DataFactory info) throws Exception {
        return ArrayUtil.toObject(input);
    }
}
