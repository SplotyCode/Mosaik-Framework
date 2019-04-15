package io.github.splotycode.mosaik.valuetransformer.io;

import io.github.splotycode.mosaik.util.ValueTransformer;

import java.net.InetAddress;

public class StringToINetAddress extends ValueTransformer<String, InetAddress> {
    @Override
    public InetAddress transform(String input) throws Exception {
        return InetAddress.getByName(input);
    }
}
