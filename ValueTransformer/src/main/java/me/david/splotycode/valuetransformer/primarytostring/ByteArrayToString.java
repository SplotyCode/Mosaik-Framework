package me.david.splotycode.valuetransformer.primarytostring;

import me.david.davidlib.link.transformer.ValueTransformer;
import me.david.davidlib.link.transformer.TransformException;

public class ByteArrayToString extends ValueTransformer<byte[], String> {

    @Override
    public String transform(byte[] input) throws TransformException {
        return new String(input);
    }

}
