package me.david.davidlib.netty;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPipeline;

import java.util.function.Consumer;

public interface INetServer {

    /*
     * void setOnInActive(Consumer<ChannelHandlerContext> consumer);
    *  void setOnActive(Consumer<ChannelHandlerContext> consumer);
    *  Consumer<ChannelHandlerContext> getOnInActive();
    *  Consumer<ChannelHandlerContext> getOnActive();
    */

    Consumer<ChannelPipeline> constructPipeline();
    void setConstructPipeline(Consumer<ChannelPipeline> constructPipeline);

    int port();
    void setPort(int port);
    ChannelFuture startServer();
    void shutDown();

    int maxConnections();
    int currentConnections();

}
