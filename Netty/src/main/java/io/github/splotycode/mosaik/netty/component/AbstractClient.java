package io.github.splotycode.mosaik.netty.component;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelPipeline;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractClient<S extends AbstractClient<S>> extends NetworkComponent<Bootstrap, Channel, S> {

    protected void prepareValues() {
        super.prepareValues();
        if (bootstrap == null) {
            bootstrap = new Bootstrap();
        }
    }

    @Override
    protected void applyChannel() {
        bootstrap.channel(channelClass);
    }

    @Override
    protected void doHandlers(ChannelPipeline pipeline) {
        ChannelInboundHandler handler = getSSLHandler(pipeline.channel());
        if (handler != null) {
            pipeline.addLast("ssl", handler);
        }
    }

    protected abstract void doDefaults();

    @Override
    protected void configureDefaults() {
        super.configureDefaults();
        doDefaults();
    }
}
