package me.david.splotycode.valuetransformer.stringtoprimary;

import me.david.davidlib.util.core.link.transformer.ValueTransformer;
import me.david.davidlib.util.core.link.transformer.TransformException;

public class StringToByte extends ValueTransformer<String, Byte> {

    @Override
    public Byte transform(String input) throws TransformException {
        byte[] bytes = input.getBytes();
        if (bytes.length != 1) throw new TransformException("Need exactly one byte");
        return bytes[0];
    }

}
