package me.david.davidlib.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.Setter;

import java.net.SocketAddress;
import java.util.function.Consumer;

public class NetClient extends Thread implements INetClient {

    @Setter private SocketAddress address;
    @Setter Consumer<ChannelPipeline> constructPipeline;

    @Getter private EventLoopGroup workerGroup;
    @Getter private Channel channel;
    @Getter @Setter private Runnable close = null;

    @Override
    public ChannelFuture startServer() {
        this.workerGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(workerGroup)
                    .channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            System.out.println("Client channel Started!");

                            ChannelPipeline pipeline = socketChannel.pipeline();
                            constructPipeline.accept(pipeline);
                        }
                    });

            this.channel = bootstrap.connect(address).sync().channel();
            ChannelFuture f = channel.closeFuture().addListener(future -> NetClient.this.workerGroup.shutdownGracefully());
            if(this.close != null) {
                f.addListener(future -> close.run());
            }
            return f;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void run() {
        startServer();
    }

    @Override
    public void shutDown() {
        channel.close();
    }

    @Override
    public Consumer<ChannelPipeline> constructPipeline() {
        return constructPipeline;
    }

    @Override
    public SocketAddress address() {
        return address;
    }
}
