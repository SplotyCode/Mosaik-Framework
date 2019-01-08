package me.david.splotycode.valuetransformer.primarytostring;

import me.david.davidlib.runtime.transformer.TransformException;
import me.david.davidlib.runtime.transformer.ValueTransformer;

public class ByteArrayToString extends ValueTransformer<byte[], String> {

    @Override
    public String transform(byte[] input) throws TransformException {
        return new String(input);
    }

}
