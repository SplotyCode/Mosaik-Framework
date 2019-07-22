package io.github.splotycode.mosaik.valuetransformer.io;

import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;

import java.net.InetAddress;

public class INetAddressToString extends ValueTransformer<InetAddress, String> {

    @Override
    public String transform(InetAddress input, DataFactory info) throws Exception {
        return StringUtil.getLastSplit(input.toString(), "/");
    }
}
