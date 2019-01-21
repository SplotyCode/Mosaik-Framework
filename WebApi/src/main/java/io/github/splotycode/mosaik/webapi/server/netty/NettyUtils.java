package io.github.splotycode.mosaik.webapi.server.netty;

import io.netty.handler.codec.http.HttpVersion;

public final class NettyUtils {

    public static HttpVersion convertHttpVersion(io.github.splotycode.mosaik.webapi.response.HttpVersion version) {
        switch (version) {
            case VERSION_1_0:
                return HttpVersion.HTTP_1_0;
            case VERSION_1_1:
                return HttpVersion.HTTP_1_1;
            default:
                throw new IllegalStateException(version.name() + " Http Version is not supported by netty");
        }
    }

    public static String transformIpAddress(String address) {
        //Normal Netty Ip Addresses: /127.0.0.1:52480
        return address.substring(1).split(":")[0];
    }

}
