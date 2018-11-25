package me.david.splotycode.valuetransformer.stringtoprimary;

import me.david.davidlib.link.transformer.IValueTransformer;
import me.david.davidlib.link.transformer.TransformException;

public class StringToShort implements IValueTransformer<String, Short> {

    @Override
    public Short transform(String input) throws TransformException {
        try {
            return Short.valueOf(input);
        } catch (NumberFormatException ex) {
            throw new TransformException("Wrong Number Format: " + input, ex);
        }
    }

}
