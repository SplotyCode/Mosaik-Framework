package io.github.splotycode.mosaik.valuetransformer.stringtoprimary;

import io.github.splotycode.mosaik.valuetransformer.TransformException;
import io.github.splotycode.mosaik.valuetransformer.ValueTransformer;

public class StringToDouble extends ValueTransformer<String, Double> {

    @Override
    public Double transform(String input) throws TransformException {
        try {
            return Double.valueOf(input);
        } catch (NumberFormatException ex) {
            throw new TransformException("Wrong Number Format: " + input, ex);
        }
    }

}
