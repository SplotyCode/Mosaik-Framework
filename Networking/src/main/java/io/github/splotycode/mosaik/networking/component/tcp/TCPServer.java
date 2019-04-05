package io.github.splotycode.mosaik.networking.component.tcp;

import io.github.splotycode.mosaik.networking.component.AbstractServer;
import io.github.splotycode.mosaik.networking.component.IServer;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuppressWarnings({"unused"})
public class TCPServer extends AbstractServer<TCPServer> implements IServer {

    public static TCPServer create() {
        return new TCPServer();
    }

    @Override
    protected void prepareValues() {
        super.prepareValues();
        if (channelClass == null) {
            channelClass = channelSystem.getServerChannelClass();
        }
    }

    @Override
    protected void configureDefaults() {
        super.configureDefaults();
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_BACKLOG, 1000)

                .childOption(ChannelOption.AUTO_READ, false)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
    }

    @Override
    protected void doHandlers(ChannelPipeline pipeline) {}


    @Override
    protected ChannelFuture doBind() {
        return bootstrap.bind(address);
    }

}
