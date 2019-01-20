package me.david.splotycode.valuetransformer.stringtoprimary;

import me.david.splotycode.valuetransformer.TransformException;
import me.david.splotycode.valuetransformer.ValueTransformer;

public class StringToByteArray extends ValueTransformer<String, byte[]> {

    @Override
    public byte[] transform(String input) throws TransformException {
        return input.getBytes();
    }

}
