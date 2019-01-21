package io.github.splotycode.mosaik.valuetransformer.stringtoprimary;

import io.github.splotycode.mosaik.valuetransformer.TransformException;
import io.github.splotycode.mosaik.valuetransformer.ValueTransformer;

public class StringToBoolen extends ValueTransformer<String, Boolean> {

    @Override
    public Boolean transform(String input) throws TransformException {
        return input != null && (input.equals("1") || input.equalsIgnoreCase("true"));
    }

}
