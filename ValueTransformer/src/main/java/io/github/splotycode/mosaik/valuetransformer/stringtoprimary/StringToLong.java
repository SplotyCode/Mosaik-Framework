package io.github.splotycode.mosaik.valuetransformer.stringtoprimary;

import io.github.splotycode.mosaik.valuetransformer.TransformException;
import io.github.splotycode.mosaik.valuetransformer.ValueTransformer;

public class StringToLong extends ValueTransformer<String, Long> {

    @Override
    public Long transform(String input) throws TransformException {
        try {
            return Long.valueOf(input);
        } catch (NumberFormatException ex) {
            throw new TransformException("Wrong Number Format: " + input, ex);
        }
    }

}
