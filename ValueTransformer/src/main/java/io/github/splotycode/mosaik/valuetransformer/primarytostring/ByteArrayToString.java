package io.github.splotycode.mosaik.valuetransformer.primarytostring;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.TransformException;

public class ByteArrayToString extends ValueTransformer<byte[], String> {

    @Override
    public String transform(byte[] input, DataFactory info) throws TransformException {
        return new String(input);
    }

}
