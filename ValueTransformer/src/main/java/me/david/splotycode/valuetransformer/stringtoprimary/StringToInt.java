package me.david.splotycode.valuetransformer.stringtoprimary;

import me.david.davidlib.link.transformer.ValueTransformer;
import me.david.davidlib.link.transformer.TransformException;

public class StringToInt extends ValueTransformer<String, Integer> {

    @Override
    public Integer transform(String input) throws TransformException {
        try {
            return Integer.valueOf(input);
        } catch (NumberFormatException ex) {
            throw new TransformException("Wrong Number Format: " + input, ex);
        }
    }

}
