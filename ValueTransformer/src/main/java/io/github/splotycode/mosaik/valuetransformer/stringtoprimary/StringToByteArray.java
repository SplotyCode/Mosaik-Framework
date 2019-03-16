package io.github.splotycode.mosaik.valuetransformer.stringtoprimary;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.valuetransformer.TransformException;

public class StringToByteArray extends ValueTransformer<String, byte[]> {

    @Override
    public byte[] transform(String input) throws TransformException {
        return input.getBytes();
    }

}
