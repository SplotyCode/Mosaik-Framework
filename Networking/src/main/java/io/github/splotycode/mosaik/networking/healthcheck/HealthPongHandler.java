package io.github.splotycode.mosaik.networking.healthcheck;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

public class HealthPongHandler extends SimpleChannelInboundHandler {

    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        return msg instanceof ByteBuf || msg instanceof HttpObject;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf content = Unpooled.copiedBuffer(new byte[] {1});
        if (msg instanceof LastHttpContent) {
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, 1);
            ctx.write(response);
        } else if (msg instanceof ByteBuf) {
            ctx.write(content);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
