package io.github.splotycode.mosaik.valuetransformer.common.primarytostring;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.exception.TransformException;

public class CharArrayToString extends ValueTransformer<char[], String> {

    @Override
    public String transform(char[] input, Class<? extends String> result, DataFactory info) throws TransformException {
        return new String(input);
    }

}
