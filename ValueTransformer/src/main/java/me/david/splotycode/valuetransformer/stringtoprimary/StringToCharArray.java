package me.david.splotycode.valuetransformer.stringtoprimary;

import me.david.davidlib.link.transformer.ValueTransformer;
import me.david.davidlib.link.transformer.TransformException;

public class StringToCharArray extends ValueTransformer<String, char[]> {

    @Override
    public char[] transform(String input) throws TransformException {
        return input.toCharArray();
    }

}
