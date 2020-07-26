package io.github.splotycode.mosaik.valuetransformer.common.primaryarray;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.collection.ArrayUtil;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;

public class LongToObjectArray extends ValueTransformer<long[], Long[]> {

    @Override
    public Long[] transform(long[] input, Class<? extends Long[]> result, DataFactory info) throws Exception {
        return ArrayUtil.toObject(input);
    }
}
