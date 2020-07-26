package io.github.splotycode.mosaik.valuetransformer.common.primaryarray;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.collection.ArrayUtil;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;

public class ByteToObjectArray extends ValueTransformer<byte[], Byte[]> {

    @Override
    public Byte[] transform(byte[] input, Class<? extends Byte[]> result, DataFactory info) throws Exception {
        return ArrayUtil.toObject(input);
    }
}
