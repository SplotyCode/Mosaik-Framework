package io.github.splotycode.mosaik.valuetransformer.common.stringtoprimary;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.exception.TransformException;

public class StringToCharArray extends ValueTransformer<String, char[]> {

    @Override
    public char[] transform(String input, Class<? extends char[]> result, DataFactory info) throws TransformException {
        return input.toCharArray();
    }

}
