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
import me.david.davidlib.netty.PacketRegistry;
import me.david.davidlib.netty.packets.SerializePacket;
import me.david.davidlib.netty.server.GenerelChannelIntilializer;

import java.net.SocketAddress;
import java.util.function.Consumer;

public class NetClient extends Thread implements INetClient {

    @Setter private SocketAddress address;
    @Setter Consumer<ChannelPipeline> constructPipeline;

    @Getter private EventLoopGroup workerGroup;

    @Getter private PacketRegistry<SerializePacket> packetRegistry;

    public NetClient(final SocketAddress address, PacketRegistry<SerializePacket> packetRegistry) {
        this.address = address;
        this.packetRegistry = packetRegistry;
    }

    @Override
    public ChannelFuture startServer() {
        this.workerGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        try {
            new Bootstrap()
                .group(workerGroup)
                .channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new GenerelChannelIntilializer(packetRegistry,  (ch) -> {
                    constructPipeline.accept(ch.pipeline());
                }))
                .connect(address).sync().channel().closeFuture().sync();
            ClientReconnectEvent event = new ClientReconnectEvent(this);
            event.callGlobal();
            System.out.println("Der Client wurde gestoppt");
            if (!event.isCanceled()) {
                System.out.println("Reconnecting in " + event.getSleepTime()/1000 + " seconds");
                Thread.sleep(event.getSleepTime());
                run();
            }
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
        workerGroup.shutdownGracefully();
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
