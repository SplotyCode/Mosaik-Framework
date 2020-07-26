package io.github.splotycode.mosaik.valuetransformer.common.io;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;

import java.nio.charset.Charset;

public class StringToCharset extends ValueTransformer<String, Charset> {
    @Override
    public Charset transform(String input, Class<? extends Charset> result, DataFactory info) throws Exception {
        return Charset.forName(input);
    }
}
