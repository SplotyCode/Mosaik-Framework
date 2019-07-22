package io.github.splotycode.mosaik.valuetransformer.primarytobinary;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.io.BinaryUtil;

public class DoubleToBinary extends ValueTransformer<Double, byte[]> {
    
    @Override
    public byte[] transform(Double input, DataFactory info) throws Exception {
        return BinaryUtil.writeDouble(input);
    }
}
