package me.david.davidlib.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import lombok.Setter;
import me.david.davidlib.netty.packets.Packet;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class NetServer<P extends Packet> extends Thread implements INetServer {

    @Getter private EventLoopGroup bossGroup, workerGroup;
    @Getter private Channel channel;
    @Getter @Setter private Runnable close;
    @Setter Consumer<ChannelPipeline> constructPipeline;

    private final AtomicInteger connections = new AtomicInteger();
    @Setter private int maxConnections;

    @Setter private int port;

    private ConnectionCounter connectionCounter = new ConnectionCounter();

    public NetServer(int port) {
        this.port = port;
    }

    @Override
    public void shutDown() {
        channel.close();
    }

    @Override
    public void run() {
        startServer();
    }

    @Override
    public ChannelFuture startServer() {
        this.bossGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        this.workerGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            System.out.println("Client connected");
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addFirst(connectionCounter);
                            constructPipeline().accept(pipeline);
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            this.channel = bootstrap.bind(port).channel();
            ChannelFuture future = this.channel.closeFuture().addListener((ChannelFutureListener) (channelFuture) -> {
                NetServer.this.bossGroup.shutdownGracefully();
                NetServer.this.workerGroup.shutdownGracefully();
            });
            if (this.close != null)
                return future.addListener((ChannelFutureListener) channelFuture -> this.close.run());
            return future;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public class ConnectionCounter extends SimpleChannelInboundHandler<P>{

        @Override protected void channelRead0(ChannelHandlerContext channelHandlerContext, P packet) throws Exception {}

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            synchronized (connections){
                int newValue = connections.intValue() - 1;
                connections.set(newValue);
            }
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            synchronized (connections){
                int newValue = connections.intValue() + 1;
                connections.set(newValue);
            }
        }
    }

    @Override
    public int port() {
        return port;
    }

    @Override
    public int maxConnections() {
        return maxConnections;
    }

    @Override
    public int currentConnections() {
        return connections.get();
    }

    @Override
    public Consumer<ChannelPipeline> constructPipeline() {
        return constructPipeline;
    }

}
