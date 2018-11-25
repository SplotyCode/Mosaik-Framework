package me.david.splotycode.valuetransformer.stringtoprimary;

import me.david.davidlib.link.transformer.ValueTransformer;
import me.david.davidlib.link.transformer.TransformException;

public class StringToBoolen extends ValueTransformer<String, Boolean> {

    @Override
    public Boolean transform(String input) throws TransformException {
        return input != null && (input.equals("1") || input.equalsIgnoreCase("true"));
    }

}
