package me.david.splotycode.valuetransformer.primarytostring;

import me.david.davidlib.link.transformer.IValueTransformer;
import me.david.davidlib.link.transformer.TransformException;

public class CharArrayToString implements IValueTransformer<char[], String> {

    @Override
    public String transform(char[] input) throws TransformException {
        return new String(input);
    }

}
