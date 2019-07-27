package io.github.splotycode.mosaik.valuetransformer.stringtoprimary;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.TransformException;

public class StringToFloat extends ValueTransformer<String, Float> {

    @Override
    public Float transform(String input, DataFactory info) throws TransformException {
        try {
            return Float.valueOf(input);
        } catch (NumberFormatException ex) {
            throw TransformException.createTranslated(info, "string_to_float", "{0} is not a in float format", ex, input);
        }
    }

}
