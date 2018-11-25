package me.david.splotycode.valuetransformer.stringtoprimary;

import me.david.davidlib.link.transformer.IValueTransformer;
import me.david.davidlib.link.transformer.TransformException;

public class StringToByteArray implements IValueTransformer<String, byte[]> {

    @Override
    public byte[] transform(String input) throws TransformException {
        return input.getBytes();
    }

}
