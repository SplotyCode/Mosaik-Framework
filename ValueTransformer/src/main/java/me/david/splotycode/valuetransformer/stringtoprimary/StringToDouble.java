package me.david.splotycode.valuetransformer.stringtoprimary;

import me.david.davidlib.link.transformer.ValueTransformer;
import me.david.davidlib.link.transformer.TransformException;

public class StringToDouble extends ValueTransformer<String, Double> {

    @Override
    public Double transform(String input) throws TransformException {
        try {
            return Double.valueOf(input);
        } catch (NumberFormatException ex) {
            throw new TransformException("Wrong Number Format: " + input, ex);
        }
    }

}
