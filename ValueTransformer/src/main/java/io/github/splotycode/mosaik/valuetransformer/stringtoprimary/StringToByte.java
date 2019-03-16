package io.github.splotycode.mosaik.valuetransformer.stringtoprimary;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.valuetransformer.TransformException;

public class StringToByte extends ValueTransformer<String, Byte> {

    @Override
    public Byte transform(String input) throws TransformException {
        byte[] bytes = input.getBytes();
        if (bytes.length != 1) throw new TransformException("Need exactly one byte");
        return bytes[0];
    }

}
