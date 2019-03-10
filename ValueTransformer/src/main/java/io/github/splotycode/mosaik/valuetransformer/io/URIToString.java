package io.github.splotycode.mosaik.valuetransformer.io;

import io.github.splotycode.mosaik.valuetransformer.TransformException;
import io.github.splotycode.mosaik.valuetransformer.ValueTransformer;

import java.net.URI;

public class URIToString extends ValueTransformer<URI, String> {

    @Override
    public String transform(URI input) throws TransformException {
        return input.toASCIIString();
    }

}
