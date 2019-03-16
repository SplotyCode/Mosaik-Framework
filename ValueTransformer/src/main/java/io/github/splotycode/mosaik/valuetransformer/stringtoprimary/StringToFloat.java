package io.github.splotycode.mosaik.valuetransformer.stringtoprimary;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.valuetransformer.TransformException;

public class StringToFloat extends ValueTransformer<String, Float> {

    @Override
    public Float transform(String input) throws TransformException {
        try {
            return Float.valueOf(input);
        } catch (NumberFormatException ex) {
            throw new TransformException("Wrong Number Format: " + input, ex);
        }
    }

}
