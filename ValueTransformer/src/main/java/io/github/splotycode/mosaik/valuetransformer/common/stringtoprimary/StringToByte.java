package io.github.splotycode.mosaik.valuetransformer.common.stringtoprimary;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.exception.TransformException;

public class StringToByte extends ValueTransformer<String, Byte> {

    @Override
    public Byte transform(String input, Class<? extends Byte> result, DataFactory info) throws TransformException {
        byte[] bytes = input.getBytes();
        if (bytes.length != 1) throw new TransformException("Need exactly one byte");
        return bytes[0];
    }

}
