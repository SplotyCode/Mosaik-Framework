package io.github.splotycode.mosaik.valuetransformer.stringtoprimary;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.TransformException;

public class StringToShort extends ValueTransformer<String, Short> {

    @Override
    public Short transform(String input, DataFactory info) throws TransformException {
        try {
            return Short.valueOf(input);
        } catch (NumberFormatException ex) {
            throw TransformException.createTranslated(info, "string_to_short", "{0} is not a in short format", ex, input);
        }
    }

}
