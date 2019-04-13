package io.github.splotycode.mosaik.networking.component.udp;

import io.github.splotycode.mosaik.networking.component.AbstractClient;
import io.github.splotycode.mosaik.networking.component.IServer;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UDPServer<S extends UDPServer<S>> extends AbstractClient<S> implements IServer {

    public static UDPServer create() {
        return new UDPServer<>();
    }

    @Override
    protected void prepareValues() {
        super.prepareValues();
        if (channelClass == null) {
            channelClass = channelSystem.getDatagramChannelClass();
        }
    }

    @Override
    protected void applyChannel() {
        bootstrap.channel(channelClass);
    }

    @Override
    protected ChannelFuture doBind() {
        return bootstrap.bind(address);
    }

    @Override
    protected void doHandlers(ChannelPipeline pipeline) {}

    @Override
    protected void doDefaults() {
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
                .option(ChannelOption.AUTO_READ, false);
    }

}
