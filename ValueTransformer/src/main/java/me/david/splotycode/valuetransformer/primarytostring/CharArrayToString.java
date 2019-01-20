package me.david.splotycode.valuetransformer.primarytostring;

import me.david.splotycode.valuetransformer.TransformException;
import me.david.splotycode.valuetransformer.ValueTransformer;

public class CharArrayToString extends ValueTransformer<char[], String> {

    @Override
    public String transform(char[] input) throws TransformException {
        return new String(input);
    }

}
