package io.github.splotycode.mosaik.networking.reverseproxy;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StaticClientProxyHandler extends AbstractClientProxyHandler {

    private Channel channel;

    @Override
    protected Channel getServerChannel(ChannelHandlerContext ctx) {
        return channel;
    }
}
