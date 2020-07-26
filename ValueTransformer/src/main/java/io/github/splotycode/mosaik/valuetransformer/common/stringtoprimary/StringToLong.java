package io.github.splotycode.mosaik.valuetransformer.common.stringtoprimary;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.exception.TransformException;

public class StringToLong extends ValueTransformer<String, Long> {

    @Override
    public Long transform(String input, Class<? extends Long> result, DataFactory info) throws TransformException {
        try {
            return Long.valueOf(input);
        } catch (NumberFormatException ex) {
            throw TransformException.createTranslated(info, "string_to_long", "{0} is not a in long format", ex, input);
        }
    }

}
