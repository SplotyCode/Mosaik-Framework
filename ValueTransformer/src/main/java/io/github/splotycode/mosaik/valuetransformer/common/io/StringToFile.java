package io.github.splotycode.mosaik.valuetransformer.common.io;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.CommonData;
import io.github.splotycode.mosaik.valuetransformer.exception.TransformException;

import java.io.File;

public class StringToFile extends ValueTransformer<String, File> {

    @Override
    public File transform(String input, Class<? extends File> result, DataFactory info) throws TransformException {
        File base = info.getDataDefault(CommonData.BASE_PATH, null);
        if (base == null) {
            return new File(input);
        }
        return new File(base, input);
    }

}
