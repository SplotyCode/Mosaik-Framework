package me.david.splotycode.valuetransformer.stringtoprimary;

import me.david.davidlib.link.transformer.IValueTransformer;
import me.david.davidlib.link.transformer.TransformException;

public class StringToChar implements IValueTransformer<String, Character> {

    @Override
    public Character transform(String input) throws TransformException {
        char[] chars = input.toCharArray();
        if (chars.length != 1) throw new TransformException("Need exactly one char");
        return chars[0];
    }

}
