package me.david.splotycode.valuetransformer.primarytostring;

import me.david.splotycode.valuetransformer.TransformException;
import me.david.splotycode.valuetransformer.ValueTransformer;

public class ByteArrayToString extends ValueTransformer<byte[], String> {

    @Override
    public String transform(byte[] input) throws TransformException {
        return new String(input);
    }

}
