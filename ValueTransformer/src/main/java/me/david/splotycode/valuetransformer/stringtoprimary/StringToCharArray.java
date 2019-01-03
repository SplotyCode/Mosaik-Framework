package me.david.splotycode.valuetransformer.stringtoprimary;

import me.david.davidlib.runtimeapi.transformer.TransformException;
import me.david.davidlib.runtimeapi.transformer.ValueTransformer;

public class StringToCharArray extends ValueTransformer<String, char[]> {

    @Override
    public char[] transform(String input) throws TransformException {
        return input.toCharArray();
    }

}
