package io.github.splotycode.mosaik.valuetransformer.common.primarytobinary;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.io.BinaryUtil;

public class FloatToBinary extends ValueTransformer<Float, byte[]> {
    
    @Override
    public byte[] transform(Float input, Class<? extends byte[]> result, DataFactory info) throws Exception {
        return BinaryUtil.writeFloat(input);
    }
}
