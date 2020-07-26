package io.github.splotycode.mosaik.valuetransformer.common.stringtoprimary;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.exception.TransformException;

public class StringToDouble extends ValueTransformer<String, Double> {

    @Override
    public Double transform(String input, Class<? extends Double> result, DataFactory info) throws TransformException {
        try {
            return Double.valueOf(input);
        } catch (NumberFormatException ex) {
            throw TransformException.createTranslated(info, "string_to_double", "{0} is not a in double format", ex, input);
        }
    }

}
