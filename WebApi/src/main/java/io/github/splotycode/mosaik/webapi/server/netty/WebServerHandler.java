package io.github.splotycode.mosaik.webapi.server.netty;

import io.github.splotycode.mosaik.webapi.config.WebConfig;
import io.github.splotycode.mosaik.webapi.request.Request;
import io.github.splotycode.mosaik.webapi.response.CookieKey;
import io.github.splotycode.mosaik.webapi.response.Response;
import io.github.splotycode.mosaik.webapi.server.BadRequestException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;

import java.util.Map;

@ChannelHandler.Sharable
public class WebServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private NettyWebServer server;

    public WebServerHandler(NettyWebServer server) {
        this.server = server;
    }

    private boolean doHttpsRedirect(FullHttpRequest request, ChannelHandlerContext ctx) {
        if (server.isSsl() && server.getConfig().getDataDefault(WebConfig.FORCE_HTTPS, false) &&
                ctx.pipeline().get(SslHandler.class) == null) {
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), HttpResponseStatus.MOVED_PERMANENTLY);
            String host = request.headers().get(HttpHeaderNames.HOST);
            if (host != null) {
                response.headers().set(HttpHeaderNames.LOCATION, "https://" + host + request.uri());
            } else {
                String hostAndPort = server.getAddress().toString();
                response.headers().set(HttpHeaderNames.LOCATION, "https://" + hostAndPort + request.uri());
            }
            ChannelFuture future = ctx.writeAndFlush(response);
            if (!HttpUtil.isKeepAlive(request)) {
                future.addListener(ChannelFutureListener.CLOSE);
            }
            return true;
        }
        return false;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        if (!msg.decoderResult().isSuccess()) {
            throw new BadRequestException("Netty Decoder Failed");
        }

        if (doHttpsRedirect(msg, ctx)) return;

        Request request = new NettyRequest(server, msg, ctx);

        long start = System.currentTimeMillis();
        Response response = server.handleRequest(request);
        response.finish(request, server);
        server.addTotalTime(System.currentTimeMillis() - start);

        ByteBuf content = Unpooled.buffer(response.getRawContent().available());
        content.writeBytes(response.getRawContent(), response.getRawContent().available());

        DefaultFullHttpResponse nettyResponse = new DefaultFullHttpResponse(
                NettyUtils.convertHttpVersion(response.getHttpVersion()),
                HttpResponseStatus.valueOf(response.getResponseCode()),
                content
        );
        for (Map.Entry<String, String> pair : response.getHeaders().entrySet()) {
            nettyResponse.headers().set(pair.getKey(), pair.getValue());
        }
        for (Map.Entry<CookieKey, String> cookie : response.getSetCookies().entrySet()) {
            nettyResponse.headers().add(HttpHeaderNames.SET_COOKIE, cookie.getKey().toHeaderString(cookie.getValue()));
        }
        ChannelFuture future = ctx.writeAndFlush(nettyResponse);
        if (!request.isKeepAlive()) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Response response = server.getErrorHandler().handleError(cause);
        response.finish(null, server);
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(response.getRawContent(), response.getRawContent().available());
        DefaultFullHttpResponse nettyResponse = new DefaultFullHttpResponse(
                NettyUtils.convertHttpVersion(response.getHttpVersion()),
                HttpResponseStatus.valueOf(response.getResponseCode()),
                byteBuf
        );
        for (Map.Entry<String, String> pair : response.getHeaders().entrySet()) {
            nettyResponse.headers().set(pair.getKey(), pair.getValue());
        }
        for (Map.Entry<CookieKey, String> cookie : response.getSetCookies().entrySet()) {
            nettyResponse.headers().add(HttpHeaderNames.SET_COOKIE, cookie.getKey().toHeaderString(cookie.getValue()));
        }
        ctx.writeAndFlush(nettyResponse).addListener(ChannelFutureListener.CLOSE);
    }
}
