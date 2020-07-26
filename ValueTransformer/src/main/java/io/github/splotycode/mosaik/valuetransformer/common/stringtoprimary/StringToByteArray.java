package io.github.splotycode.mosaik.valuetransformer.common.stringtoprimary;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.CommonData;
import io.github.splotycode.mosaik.valuetransformer.exception.TransformException;

import java.nio.charset.Charset;

public class StringToByteArray extends ValueTransformer<String, byte[]> {

    @Override
    public byte[] transform(String input, Class<? extends byte[]> result, DataFactory info) throws TransformException {
        Charset charset = info.getDataDefault(CommonData.CHARSET, null);
        if (charset == null) {
            return input.getBytes();
        }
        return input.getBytes(charset);
    }

}
