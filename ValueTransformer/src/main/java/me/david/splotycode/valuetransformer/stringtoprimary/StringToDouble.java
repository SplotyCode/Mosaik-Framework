package me.david.splotycode.valuetransformer.stringtoprimary;

import me.david.splotycode.valuetransformer.TransformException;
import me.david.splotycode.valuetransformer.ValueTransformer;

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
