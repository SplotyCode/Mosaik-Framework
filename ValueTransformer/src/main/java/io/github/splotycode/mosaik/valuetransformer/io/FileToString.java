package io.github.splotycode.mosaik.valuetransformer.io;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.TransformException;

import java.io.File;

public class FileToString extends ValueTransformer<File, String> {

    @Override
    public String transform(File input, DataFactory info) throws TransformException {
        return input.getAbsolutePath();
    }

}
