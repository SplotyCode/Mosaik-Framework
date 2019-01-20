package me.david.splotycode.valuetransformer.stringtoprimary;

import me.david.splotycode.valuetransformer.TransformException;
import me.david.splotycode.valuetransformer.ValueTransformer;

public class StringToByte extends ValueTransformer<String, Byte> {

    @Override
    public Byte transform(String input) throws TransformException {
        byte[] bytes = input.getBytes();
        if (bytes.length != 1) throw new TransformException("Need exactly one byte");
        return bytes[0];
    }

}
