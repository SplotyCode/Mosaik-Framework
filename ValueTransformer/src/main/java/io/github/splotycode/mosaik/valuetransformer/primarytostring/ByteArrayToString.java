package io.github.splotycode.mosaik.valuetransformer.primarytostring;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.CommonData;
import io.github.splotycode.mosaik.valuetransformer.TransformException;

import java.nio.charset.Charset;

public class ByteArrayToString extends ValueTransformer<byte[], String> {

    @Override
    public String transform(byte[] input, DataFactory info) throws TransformException {
        Charset charset = info.getDataDefault(CommonData.CHARSET, null);
        if (charset == null) {
            return new String(input);
        }
        return new String(input, charset);
    }

}
