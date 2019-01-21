package io.github.splotycode.mosaik.valuetransformer.stringtoprimary;

import io.github.splotycode.mosaik.valuetransformer.TransformException;
import io.github.splotycode.mosaik.valuetransformer.ValueTransformer;

public class StringToCharArray extends ValueTransformer<String, char[]> {

    @Override
    public char[] transform(String input) throws TransformException {
        return input.toCharArray();
    }

}
