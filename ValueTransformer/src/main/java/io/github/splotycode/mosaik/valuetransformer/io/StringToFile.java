package io.github.splotycode.mosaik.valuetransformer.io;

import io.github.splotycode.mosaik.valuetransformer.TransformException;
import io.github.splotycode.mosaik.valuetransformer.ValueTransformer;

import java.io.File;

public class StringToFile extends ValueTransformer<String, File> {

    @Override
    public File transform(String input) throws TransformException {
        return new File(input);
    }

}
