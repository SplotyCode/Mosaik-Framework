package me.david.splotycode.valuetransformer.stringtoprimary;

import me.david.davidlib.link.transformer.ValueTransformer;
import me.david.davidlib.link.transformer.TransformException;

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
