package me.david.splotycode.valuetransformer.stringtoprimary;

import me.david.davidlib.runtimeapi.transformer.TransformException;
import me.david.davidlib.runtimeapi.transformer.ValueTransformer;

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
