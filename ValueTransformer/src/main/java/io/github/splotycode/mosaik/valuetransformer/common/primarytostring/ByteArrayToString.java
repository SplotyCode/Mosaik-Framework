package io.github.splotycode.mosaik.valuetransformer.common.primarytostring;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.CommonData;
import io.github.splotycode.mosaik.valuetransformer.exception.TransformException;

import java.nio.charset.Charset;

public class ByteArrayToString extends ValueTransformer<byte[], String> {

    @Override
    public String transform(byte[] input, Class<? extends String> result, DataFactory info) throws TransformException {
        Charset charset = info.getDataDefault(CommonData.CHARSET, null);
        if (charset == null) {
            return new String(input);
        }
        return new String(input, charset);
    }

}
