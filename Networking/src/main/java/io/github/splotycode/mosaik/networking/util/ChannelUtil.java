package io.github.splotycode.mosaik.networking.util;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChannelUtil {

    public static void writeAndClose(Channel channel, Object msg) {
        channel.writeAndFlush(msg).addListener(ChannelFutureListener.CLOSE);
    }

    public static void writeAndClose(ChannelHandlerContext ctx, Object msg) {
        writeAndClose(ctx.channel(), msg);
    }

    public static void closeQuietly(Channel channel) {
        writeAndClose(channel, Unpooled.EMPTY_BUFFER);
    }

    public static void closeQuietly(ChannelHandlerContext ctx) {
        closeQuietly(ctx.channel());
    }

}
