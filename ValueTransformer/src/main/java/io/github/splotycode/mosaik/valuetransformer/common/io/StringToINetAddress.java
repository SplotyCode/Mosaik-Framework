package io.github.splotycode.mosaik.valuetransformer.common.io;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;

import java.net.InetAddress;

public class StringToINetAddress extends ValueTransformer<String, InetAddress> {
    @Override
    public InetAddress transform(String input, Class<? extends InetAddress> result, DataFactory info) throws Exception {
        return InetAddress.getByName(input);
    }
}
