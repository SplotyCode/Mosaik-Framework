package io.github.splotycode.mosaik.valuetransformer.io;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.TransformException;

import java.net.URI;
import java.net.URISyntaxException;

public class StringToURI extends ValueTransformer<String, URI> {

    @Override
    public URI transform(String input, DataFactory info) throws TransformException {
        try {
            return new URI(input);
        } catch (URISyntaxException e) {
            throw new TransformException("Syntax error in URI", e);
        }
    }

}
