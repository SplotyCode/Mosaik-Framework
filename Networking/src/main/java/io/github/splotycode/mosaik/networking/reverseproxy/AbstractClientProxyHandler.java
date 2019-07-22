package io.github.splotycode.mosaik.networking.reverseproxy;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpHeaderNames;


public abstract class AbstractClientProxyHandler extends SimpleChannelInboundHandler<FullHttpMessage> {

    private Channel server;

    protected abstract Channel getServerChannel(ChannelHandlerContext ctx);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpMessage msg) throws Exception {
        if (server.isActive()) {
            msg.headers().set(HttpHeaderNames.HOST, server.remoteAddress().toString());
            server.writeAndFlush(msg.retain());
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        server = getServerChannel(ctx);
        if (server == null) {
            ctx.close();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        server.close();
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        channelInactive(ctx);
    }
}
