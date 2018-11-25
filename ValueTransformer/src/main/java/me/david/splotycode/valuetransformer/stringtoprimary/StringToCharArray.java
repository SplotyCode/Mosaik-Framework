package me.david.splotycode.valuetransformer.stringtoprimary;

import me.david.davidlib.link.transformer.IValueTransformer;
import me.david.davidlib.link.transformer.TransformException;

public class StringToCharArray implements IValueTransformer<String, char[]> {

    @Override
    public char[] transform(String input) throws TransformException {
        return input.toCharArray();
    }

}
