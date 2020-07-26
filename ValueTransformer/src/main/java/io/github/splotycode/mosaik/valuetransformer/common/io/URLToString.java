package io.github.splotycode.mosaik.valuetransformer.common.io;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.exception.TransformException;

import java.net.URL;

public class URLToString extends ValueTransformer<URL, String> {

    @Override
    public String transform(URL input, Class<? extends String> result, DataFactory info) throws TransformException {
        return input.toExternalForm();
    }

}
