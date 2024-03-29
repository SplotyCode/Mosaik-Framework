package io.github.splotycode.mosaik.valuetransformer.common.primarytobinary;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.io.BinaryUtil;

public class ShortToBinary extends ValueTransformer<Short, byte[]> {
    
    @Override
    public byte[] transform(Short input, Class<? extends byte[]> result, DataFactory info) throws Exception {
        return BinaryUtil.writeShort(input);
    }
}
