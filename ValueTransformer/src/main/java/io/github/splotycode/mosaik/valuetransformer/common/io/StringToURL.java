package io.github.splotycode.mosaik.valuetransformer.common.io;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.exception.TransformException;

import java.net.MalformedURLException;
import java.net.URL;

public class StringToURL extends ValueTransformer<String, URL> {
    @Override
    public URL transform(String input, Class<? extends URL> result, DataFactory info) throws TransformException {
        try {
            return new URL(input);
        } catch (MalformedURLException e) {
            throw TransformException.createTranslated(info, "string_to_url", "Syntax error in URL", e);
        }
    }
}
