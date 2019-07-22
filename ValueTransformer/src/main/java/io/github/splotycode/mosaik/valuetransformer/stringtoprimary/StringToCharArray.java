package io.github.splotycode.mosaik.valuetransformer.stringtoprimary;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.TransformException;

public class StringToCharArray extends ValueTransformer<String, char[]> {

    @Override
    public char[] transform(String input, DataFactory info) throws TransformException {
        return input.toCharArray();
    }

}
