package io.github.splotycode.mosaik.valuetransformer.io;

import io.github.splotycode.mosaik.valuetransformer.TransformException;
import io.github.splotycode.mosaik.valuetransformer.ValueTransformer;

import java.net.URI;
import java.net.URISyntaxException;

public class StringToURI extends ValueTransformer<String, URI> {

    @Override
    public URI transform(String input) throws TransformException {
        try {
            return new URI(input);
        } catch (URISyntaxException e) {
            throw new TransformException("Syntax error in URI", e);
        }
    }

}
