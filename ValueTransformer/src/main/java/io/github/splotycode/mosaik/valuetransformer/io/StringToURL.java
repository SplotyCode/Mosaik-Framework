package io.github.splotycode.mosaik.valuetransformer.io;

import io.github.splotycode.mosaik.valuetransformer.TransformException;
import io.github.splotycode.mosaik.valuetransformer.ValueTransformer;

import java.net.MalformedURLException;
import java.net.URL;

public class StringToURL extends ValueTransformer<String, URL> {
    @Override
    public URL transform(String input) throws TransformException {
        try {
            return new URL(input);
        } catch (MalformedURLException e) {
            throw new TransformException("Syntax error in URL", e);
        }
    }
}
