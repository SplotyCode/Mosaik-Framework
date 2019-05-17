package io.github.splotycode.mosaik.networking.reverseproxy;

import io.github.splotycode.mosaik.networking.component.template.ServerTemplate;
import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpObjectAggregator;


public class ClientProxyHandler extends SimpleChannelInboundHandler<FullHttpMessage> {

    private Channel server;
    private ServerTemplate template;

    public ClientProxyHandler(ServerTemplate template) {
        this.template = template;
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
        server = template.createComponent().handler(5, "init", new ChannelInitializer<Channel>() {
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
