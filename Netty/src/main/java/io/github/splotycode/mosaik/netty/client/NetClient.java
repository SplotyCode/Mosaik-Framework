package io.github.splotycode.mosaik.netty.client;

import io.github.splotycode.mosaik.netty.server.GeneralChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.Setter;
import io.github.splotycode.mosaik.util.logger.Logger;
import io.github.splotycode.mosaik.netty.packet.packets.SerializePacket;
import io.github.splotycode.mosaik.netty.packet.PacketRegistry;

import java.net.SocketAddress;
import java.util.function.Consumer;

public class NetClient extends Thread implements INetClient {

    private Logger logger = Logger.getInstance(getClass());

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
                .handler(new GeneralChannelInitializer(packetRegistry,  (ch) -> {
                    constructPipeline.accept(ch.pipeline());
                }))
                .connect(address).sync().channel().closeFuture().sync();
            ClientReconnectEvent event = new ClientReconnectEvent(this);
            event.callGlobal();
            logger.info("The Client is stopped");
            if (!event.isCanceled()) {
                logger.info("Reconnecting in " + event.getSleepTime()/1000 + " seconds");
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
