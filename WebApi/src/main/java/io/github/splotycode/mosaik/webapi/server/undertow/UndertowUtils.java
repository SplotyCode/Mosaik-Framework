package io.github.splotycode.mosaik.webapi.server.undertow;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpVersion;
import io.undertow.server.HttpServerExchange;
import io.github.splotycode.mosaik.util.io.ByteArrayInputStream;
import io.github.splotycode.mosaik.util.io.IOUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UndertowUtils {

    public static void send(HttpServerExchange exchange, InputStream stream) throws IOException {
        byte[] bytes;
        if (stream instanceof ByteArrayInputStream && ((ByteArrayInputStream) stream).isOriginal()) {
            bytes = ((ByteArrayInputStream) stream).getBuf();
        } else {
            bytes = IOUtil.toByteArray(stream);
        }
        exchange.getResponseSender().send(ByteBuffer.wrap(bytes));
    }

    public static boolean isKeepAlive(HttpServerExchange exchange) {
        String connection = exchange.getRequestHeaders().get("connection").getFirst();
        if (connection != null && HttpHeaderValues.CLOSE.contentEqualsIgnoreCase(connection)) {
            return false;
        } else if (toNettyVersion(exchange).isKeepAliveDefault()) {
            return !HttpHeaderValues.CLOSE.contentEqualsIgnoreCase(connection);
        } else {
            return HttpHeaderValues.KEEP_ALIVE.contentEqualsIgnoreCase(connection);
        }
    }

    public static HttpVersion toNettyVersion(HttpServerExchange exchange) {
        if (exchange.isHttp10()) {
            return HttpVersion.HTTP_1_0;
        }
        return HttpVersion.HTTP_1_1;
    }

}
