package io.github.splotycode.mosaik.webapi.server.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ServerChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NettyChannelInitializer extends ChannelInitializer<ServerChannel> {

    private NettyWebServer webServer;

    @Override
    protected void initChannel(ServerChannel channel) {
        final ChannelPipeline p = channel.pipeline();
        p.addFirst(new LoggingHandler(LogLevel.DEBUG));
        if (webServer.getSslContext() != null) {
            p.addLast(webServer.getSslContext().newHandler(channel.alloc()));
        }
        p.addLast(new HttpRequestDecoder());
        p.addLast("aggregator", new HttpObjectAggregator(512 * 1024));
        p.addLast(new HttpResponseEncoder());
        p.addLast(new HttpContentCompressor());
        p.addLast("handler", webServer.getHandler());
    }
}
