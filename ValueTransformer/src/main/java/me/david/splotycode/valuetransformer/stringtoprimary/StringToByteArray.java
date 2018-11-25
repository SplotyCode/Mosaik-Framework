package me.david.splotycode.valuetransformer.stringtoprimary;

import me.david.davidlib.link.transformer.ValueTransformer;
import me.david.davidlib.link.transformer.TransformException;

public class StringToByteArray extends ValueTransformer<String, byte[]> {

    @Override
    public byte[] transform(String input) throws TransformException {
        return input.getBytes();
    }

}
