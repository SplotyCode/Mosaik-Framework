package io.github.splotycode.mosaik.valuetransformer.io;

import io.github.splotycode.mosaik.valuetransformer.TransformException;
import io.github.splotycode.mosaik.valuetransformer.ValueTransformer;

import java.net.URL;

public class URLToString extends ValueTransformer<URL, String> {

    @Override
    public String transform(URL input) throws TransformException {
        return input.toExternalForm();
    }

}
