package io.github.splotycode.mosaik.valuetransformer.common.primarytobinary;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.io.BinaryUtil;

public class IntToBinary extends ValueTransformer<Integer, byte[]> {

    @Override
    public byte[] transform(Integer input, Class<? extends byte[]> result, DataFactory info) throws Exception {
        return BinaryUtil.writeInt(input);
    }
}
