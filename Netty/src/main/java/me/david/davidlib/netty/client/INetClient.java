package me.david.davidlib.netty.client;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPipeline;

import java.net.SocketAddress;
import java.util.function.Consumer;

public interface INetClient {

    Consumer<ChannelPipeline> constructPipeline();
    void setConstructPipeline(Consumer<ChannelPipeline> constructPipeline);

    SocketAddress address();
    void setAddress(SocketAddress port);
    ChannelFuture startServer();
    void shutDown();

}
