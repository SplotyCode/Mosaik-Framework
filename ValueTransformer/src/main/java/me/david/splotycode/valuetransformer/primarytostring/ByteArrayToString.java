package me.david.splotycode.valuetransformer.primarytostring;

import me.david.davidlib.util.core.link.transformer.ValueTransformer;
import me.david.davidlib.util.core.link.transformer.TransformException;

public class ByteArrayToString extends ValueTransformer<byte[], String> {

    @Override
    public String transform(byte[] input) throws TransformException {
        return new String(input);
    }

}
