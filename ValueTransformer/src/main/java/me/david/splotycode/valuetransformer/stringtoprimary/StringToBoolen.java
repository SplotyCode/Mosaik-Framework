package me.david.splotycode.valuetransformer.stringtoprimary;

import me.david.davidlib.link.transformer.IValueTransformer;
import me.david.davidlib.link.transformer.TransformException;

public class StringToBoolen implements IValueTransformer<String, Boolean> {

    @Override
    public Boolean transform(String input) throws TransformException {
        return input != null && (input.equals("1") || input.equalsIgnoreCase("true"));
    }

}
