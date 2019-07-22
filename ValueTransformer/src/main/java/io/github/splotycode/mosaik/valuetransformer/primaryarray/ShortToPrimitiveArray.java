package io.github.splotycode.mosaik.valuetransformer.primaryarray;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.collection.ArrayUtil;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;

public class ShortToPrimitiveArray extends ValueTransformer<Short[], short[]> {

    @Override
    public short[] transform(Short[] input, DataFactory info) throws Exception {
        return ArrayUtil.toPrimitive(input);
    }
}
