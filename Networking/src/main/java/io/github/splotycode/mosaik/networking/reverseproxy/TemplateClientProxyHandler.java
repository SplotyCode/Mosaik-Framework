package io.github.splotycode.mosaik.networking.reverseproxy;

import io.github.splotycode.mosaik.networking.component.template.ServerTemplate;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TemplateClientProxyHandler extends AbstractClientProxyHandler {

    private ServerTemplate template;

    @Override
    protected Channel getServerChannel(ChannelHandlerContext ctx) {
        return template.createComponent().handler(5, "init", new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) {
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
}
