package io.github.splotycode.mosaik.valuetransformer.primaryarray;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.collection.ArrayUtil;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;

public class LongToPrimitiveArray extends ValueTransformer<Long[], long[]> {

    @Override
    public long[] transform(Long[] input, DataFactory info) throws Exception {
        return ArrayUtil.toPrimitive(input);
    }
}
