package io.github.splotycode.mosaik.netty.reverseproxy;

import io.github.splotycode.mosaik.netty.component.tcp.TCPServer;
import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpObjectAggregator;

import java.net.SocketAddress;
import java.util.function.Supplier;


public class ClientProxyHandler extends SimpleChannelInboundHandler<FullHttpMessage> {

    private Channel server;
    private Supplier<SocketAddress> connectTo;

    public ClientProxyHandler(Supplier<SocketAddress> connectTo) {
        this.connectTo = connectTo;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpMessage msg) throws Exception {
        if (server.isActive()) {
            msg.headers().set(HttpHeaderNames.HOST, server.remoteAddress().toString());
            server.writeAndFlush(msg.retain());
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        server = TCPServer.create().address(connectTo.get()).handler("init", new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                ChannelPipeline pipeline = channel.pipeline();
                pipeline.addLast(new HttpClientCodec());
                pipeline.addLast(new HttpObjectAggregator(8192, true));
                pipeline.addLast(new ServerProxyHandler(ctx.channel()));
            }
        }).onBound((component, future) -> {
            if (future.isSuccess()) {
                ctx.read();
            } else {
                ctx.close();
            }
        }).bind().nettyFuture().channel();
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
