package io.github.splotycode.mosaik.networking.component.http;

import io.github.splotycode.mosaik.valuetransformer.io.SocketAddressToString;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

public class HttpRedirectHandler extends SimpleChannelInboundHandler<HttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest request) throws Exception {
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), HttpResponseStatus.MOVED_PERMANENTLY);
        String host = request.headers().get(HttpHeaderNames.HOST);
        if (host == null) {
            host = new SocketAddressToString().transform(ctx.channel().localAddress());
        }
        response.headers().set(HttpHeaderNames.LOCATION, "https://" + host + request.uri());

        ChannelFuture future = ctx.writeAndFlush(response);
        if (!HttpUtil.isKeepAlive(request)) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

}
