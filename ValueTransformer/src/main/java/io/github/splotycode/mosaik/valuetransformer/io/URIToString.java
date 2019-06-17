package io.github.splotycode.mosaik.valuetransformer.io;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.TransformException;

import java.net.URI;

public class URIToString extends ValueTransformer<URI, String> {

    @Override
    public String transform(URI input, DataFactory info) throws TransformException {
        return input.toASCIIString();
    }

}
