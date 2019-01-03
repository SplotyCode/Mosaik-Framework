package me.david.splotycode.valuetransformer.primarytostring;

import me.david.davidlib.util.core.link.transformer.ValueTransformer;
import me.david.davidlib.util.core.link.transformer.TransformException;

public class CharArrayToString extends ValueTransformer<char[], String> {

    @Override
    public String transform(char[] input) throws TransformException {
        return new String(input);
    }

}
