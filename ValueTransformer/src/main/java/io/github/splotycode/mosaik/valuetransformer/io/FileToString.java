package io.github.splotycode.mosaik.valuetransformer.io;

import io.github.splotycode.mosaik.valuetransformer.TransformException;
import io.github.splotycode.mosaik.valuetransformer.ValueTransformer;

import java.io.File;

public class FileToString extends ValueTransformer<File, String> {

    @Override
    public String transform(File input) throws TransformException {
        return input.getAbsolutePath();
    }

}
