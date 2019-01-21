package io.github.splotycode.mosaik.valuetransformer.stringtoprimary;

import io.github.splotycode.mosaik.valuetransformer.TransformException;
import io.github.splotycode.mosaik.valuetransformer.ValueTransformer;

public class StringToShort extends ValueTransformer<String, Short> {

    @Override
    public Short transform(String input) throws TransformException {
        try {
            return Short.valueOf(input);
        } catch (NumberFormatException ex) {
            throw new TransformException("Wrong Number Format: " + input, ex);
        }
    }

}
