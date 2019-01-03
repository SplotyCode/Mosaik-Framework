package me.david.davidlib.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import lombok.Setter;
import me.david.davidlib.netty.packets.Packet;
import me.david.davidlib.netty.packets.SerializePacket;
import me.david.davidlib.netty.PacketRegistry;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class NetServer<P extends Packet> extends Thread implements INetServer {

    @Getter private EventLoopGroup bossGroup, workerGroup;
    @Setter Consumer<ChannelPipeline> constructPipeline;

    private final AtomicInteger connections = new AtomicInteger();
    @Setter private int maxConnections;

    @Setter private int port;

    private ConnectionCounter connectionCounter = new ConnectionCounter();

    @Getter private PacketRegistry<SerializePacket> packetRegistry;

    public NetServer(int port, PacketRegistry<SerializePacket> packetRegistry) {
        this.port = port;
        this.packetRegistry = packetRegistry;
    }

    @Override
    public void shutDown() {
        NetServer.this.bossGroup.shutdownGracefully();
        NetServer.this.workerGroup.shutdownGracefully();
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
                    .childHandler(new GeneralChannelInitializer(packetRegistry,  (ch) -> {
                        ch.pipeline().addLast(connectionCounter);
                        constructPipeline.accept(ch.pipeline());
                    }))
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @ChannelHandler.Sharable
    public class ConnectionCounter extends SimpleChannelInboundHandler {

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

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

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
