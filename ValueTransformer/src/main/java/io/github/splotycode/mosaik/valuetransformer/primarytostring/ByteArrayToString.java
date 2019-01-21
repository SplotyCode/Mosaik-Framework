package io.github.splotycode.mosaik.valuetransformer.primarytostring;

import io.github.splotycode.mosaik.valuetransformer.TransformException;
import io.github.splotycode.mosaik.valuetransformer.ValueTransformer;

public class ByteArrayToString extends ValueTransformer<byte[], String> {

    @Override
    public String transform(byte[] input) throws TransformException {
        return new String(input);
    }

}
