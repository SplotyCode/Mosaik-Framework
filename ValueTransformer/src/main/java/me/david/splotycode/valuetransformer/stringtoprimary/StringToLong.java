package me.david.splotycode.valuetransformer.stringtoprimary;

import me.david.davidlib.link.transformer.IValueTransformer;
import me.david.davidlib.link.transformer.TransformException;

public class StringToLong implements IValueTransformer<String, Long> {

    @Override
    public Long transform(String input) throws TransformException {
        try {
            return Long.valueOf(input);
        } catch (NumberFormatException ex) {
            throw new TransformException("Wrong Number Format: " + input, ex);
        }
    }

}
