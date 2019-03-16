package io.github.splotycode.mosaik.valuetransformer.io;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.valuetransformer.TransformException;

import java.net.URL;

public class URLToString extends ValueTransformer<URL, String> {

    @Override
    public String transform(URL input) throws TransformException {
        return input.toExternalForm();
    }

}
