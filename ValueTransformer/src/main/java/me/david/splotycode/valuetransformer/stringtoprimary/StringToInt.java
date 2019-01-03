package me.david.splotycode.valuetransformer.stringtoprimary;

import me.david.davidlib.runtimeapi.transformer.TransformException;
import me.david.davidlib.runtimeapi.transformer.ValueTransformer;

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
