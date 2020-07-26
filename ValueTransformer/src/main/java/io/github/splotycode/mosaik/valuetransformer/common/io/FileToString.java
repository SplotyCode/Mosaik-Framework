package io.github.splotycode.mosaik.valuetransformer.common.io;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.exception.TransformException;

import java.io.File;

public class FileToString extends ValueTransformer<File, String> {

    @Override
    public String transform(File input, Class<? extends String> result, DataFactory info) throws TransformException {
        return input.getAbsolutePath();
    }

}
