package io.github.splotycode.mosaik.valuetransformer.primarytobinary;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.io.BinaryUtil;

public class LongToBinary extends ValueTransformer<Long, byte[]> {

    @Override
    public byte[] transform(Long input, DataFactory info) throws Exception {
        return BinaryUtil.writeLong(input);
    }
}
