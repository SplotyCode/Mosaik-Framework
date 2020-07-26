package io.github.splotycode.mosaik.valuetransformer.common.io;

import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;

import java.net.InetAddress;

public class INetAddressToString extends ValueTransformer<InetAddress, String> {

    @Override
    public String transform(InetAddress input, Class<? extends String> result, DataFactory info) throws Exception {
        return StringUtil.getLastSplit(input.toString(), "/");
    }
}
