package io.github.splotycode.mosaik.netty.reverseproxy;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpMessage;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ServerProxyHandler extends SimpleChannelInboundHandler<FullHttpMessage> {

    private Channel client;

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
        client.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        channelInactive(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.read();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpMessage msg) throws Exception {
        ctx.writeAndFlush(msg).addListener(future -> {
           ctx.close();
           client.close();
        });
    }
}
