package io.github.splotycode.mosaik.valuetransformer.common.stringtoprimary;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.exception.TransformException;

public class StringToShort extends ValueTransformer<String, Short> {

    @Override
    public Short transform(String input, Class<? extends Short> result, DataFactory info) throws TransformException {
        try {
            return Short.valueOf(input);
        } catch (NumberFormatException ex) {
            throw TransformException.createTranslated(info, "string_to_short", "{0} is not a in short format", ex, input);
        }
    }

}
