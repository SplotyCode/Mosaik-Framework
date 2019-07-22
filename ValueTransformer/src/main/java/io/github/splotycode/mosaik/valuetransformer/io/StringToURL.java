package io.github.splotycode.mosaik.valuetransformer.io;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.TransformException;

import java.net.MalformedURLException;
import java.net.URL;

public class StringToURL extends ValueTransformer<String, URL> {
    @Override
    public URL transform(String input, DataFactory info) throws TransformException {
        try {
            return new URL(input);
        } catch (MalformedURLException e) {
            throw new TransformException("Syntax error in URL", e);
        }
    }
}
