package io.github.splotycode.mosaik.valuetransformer.primarytobinary;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.io.BinaryUtil;

public class CharToBinary extends ValueTransformer<Character, byte[]> {

    @Override
    public byte[] transform(Character input, DataFactory info) throws Exception {
        return BinaryUtil.writeChar(input);
    }
}
