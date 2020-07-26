package io.github.splotycode.mosaik.valuetransformer.common.stringtoprimary;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.exception.TransformException;

public class StringToBoolean extends ValueTransformer<String, Boolean> {

    @Override
    public Boolean transform(String input, Class<? extends Boolean> result, DataFactory info) throws TransformException {
        return input != null && (input.equals("1") || input.equalsIgnoreCase("true"));
    }

}
