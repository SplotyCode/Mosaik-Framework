package me.david.splotycode.valuetransformer.stringtoprimary;

import me.david.davidlib.runtime.transformer.TransformException;
import me.david.davidlib.runtime.transformer.ValueTransformer;

public class StringToByteArray extends ValueTransformer<String, byte[]> {

    @Override
    public byte[] transform(String input) throws TransformException {
        return input.getBytes();
    }

}
