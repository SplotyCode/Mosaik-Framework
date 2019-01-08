package me.david.splotycode.valuetransformer.primarytostring;

import me.david.davidlib.runtime.transformer.TransformException;
import me.david.davidlib.runtime.transformer.ValueTransformer;

public class CharArrayToString extends ValueTransformer<char[], String> {

    @Override
    public String transform(char[] input) throws TransformException {
        return new String(input);
    }

}
