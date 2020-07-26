package io.github.splotycode.mosaik.valuetransformer.common.stringtoprimary;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.exception.TransformException;

public class StringToInt extends ValueTransformer<String, Integer> {

    @Override
    public Integer transform(String input, Class<? extends Integer> result, DataFactory info) throws TransformException {
        try {
            return Integer.valueOf(input);
        } catch (NumberFormatException ex) {
            throw TransformException.createTranslated(info, "string_to_int", "{0} is not a in integer format", ex, input);
        }
    }

}
