package io.github.splotycode.mosaik.networking.util;

import io.github.splotycode.mosaik.valuetransformer.TransformException;
import io.github.splotycode.mosaik.valuetransformer.io.SocketAddressToString;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.net.SocketAddress;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AddressSerializer {

    private static final SocketAddressToString SOCKET_ADDRESS_SERIALIZER = new SocketAddressToString();

    public static String toString(SocketAddress address) {
        try {
            return SOCKET_ADDRESS_SERIALIZER.transform(address);
        } catch (Exception e) {
            throw new TransformException(e);
        }
    }

}
