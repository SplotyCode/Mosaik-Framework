package io.github.splotycode.mosaik.valuetransformer.io;

import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.TransformException;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class StringToSocketAddress extends ValueTransformer<String, SocketAddress> {

    @Override
    public SocketAddress transform(String input, DataFactory info) throws Exception {
        String[] split = StringUtil.getLastSplit(input, "/").split(":");
        if (split.length == 2) {
            return new InetSocketAddress(split[0], Integer.valueOf(split[1]));
        } else {
            throw new TransformException("Illegal Format");
        }
    }
}
