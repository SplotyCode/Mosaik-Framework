package io.github.splotycode.mosaik.webapi.server.netty;

import io.github.splotycode.mosaik.webapi.config.WebConfig;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.OptionalSslHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NettyChannelInitializer extends ChannelInitializer {

    protected NettyWebServer webServer;

    @Override
    protected void initChannel(Channel channel) {
        final ChannelPipeline p = channel.pipeline();
        p.addFirst(new LoggingHandler(LogLevel.DEBUG));
        if (webServer.getSslContext() != null) {
            if (webServer.getConfig().getDataDefault(WebConfig.IGNORE_NO_SSL_RECORD, false)) {
                p.addFirst(new OptionalSslHandler(webServer.getSslContext()));
            } else {
                p.addFirst(webServer.getSslContext().newHandler(channel.alloc()));
            }
        }
        p.addLast(new HttpRequestDecoder());
        p.addLast("aggregator", new HttpObjectAggregator(512 * 1024));
        p.addLast(new HttpResponseEncoder());
        p.addLast(new HttpContentCompressor());
        p.addLast("handler", webServer.getHandler());
    }
}
