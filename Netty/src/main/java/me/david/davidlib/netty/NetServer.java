package me.david.davidlib.netty;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

import java.util.function.Consumer;

public interface NetServer {

    void shutDown();
    void setOnInActiv(Consumer<ChannelHandlerContext> consumer);
    void setOnActiv(Consumer<ChannelHandlerContext> consumer);
    int port();
    ChannelFuture start();

}
