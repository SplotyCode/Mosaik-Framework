package me.david.splotycode.valuetransformer.stringtoprimary;

import me.david.splotycode.valuetransformer.TransformException;
import me.david.splotycode.valuetransformer.ValueTransformer;

public class StringToCharArray extends ValueTransformer<String, char[]> {

    @Override
    public char[] transform(String input) throws TransformException {
        return input.toCharArray();
    }

}
