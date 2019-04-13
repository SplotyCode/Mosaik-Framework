package io.github.splotycode.mosaik.valuetransformer.io;

import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.ValueTransformer;

import java.net.SocketAddress;

public class SocketAddressToString extends ValueTransformer<SocketAddress, String> {
    @Override
    public String transform(SocketAddress input) throws Exception {
        return StringUtil.getLastSplit(input.toString(), "/");
    }
}
