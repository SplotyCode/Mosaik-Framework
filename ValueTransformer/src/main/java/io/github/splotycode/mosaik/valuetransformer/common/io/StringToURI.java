package io.github.splotycode.mosaik.valuetransformer.common.io;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.exception.TransformException;

import java.net.URI;
import java.net.URISyntaxException;

public class StringToURI extends ValueTransformer<String, URI> {

    @Override
    public URI transform(String input, Class<? extends URI> result, DataFactory info) throws TransformException {
        try {
            return new URI(input);
        } catch (URISyntaxException e) {
            throw TransformException.createTranslated(info, "string_to_uri", "Syntax error in URI", e);
        }
    }

}
