package io.github.splotycode.mosaik.netty.component;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.*;
import io.netty.channel.kqueue.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;

public enum ChannelSystem {

    NIO(NioSocketChannel.class, NioServerSocketChannel.class, NioDatagramChannel.class) {
        @Override
        public EventLoopGroup newLoopGroup() {
            return new NioEventLoopGroup();
        }

        @Override
        public EventLoopGroup newLoopGroup(int nThreads) {
            return new NioEventLoopGroup(nThreads);
        }

        @Override
        public boolean isAvailable() {
            return true;
        }
    },
    EPOLL(EpollSocketChannel.class, EpollServerSocketChannel.class, EpollDatagramChannel.class) {
        @Override
        public EventLoopGroup newLoopGroup() {
            return new EpollEventLoopGroup();
        }

        @Override
        public EventLoopGroup newLoopGroup(int nThreads) {
            return new EpollEventLoopGroup(nThreads);
        }

        @Override
        public boolean isAvailable() {
            return Epoll.isAvailable();
        }
    },
    K_QUEUNE(KQueueSocketChannel.class, KQueueServerSocketChannel.class, KQueueDatagramChannel.class) {
        @Override
        public EventLoopGroup newLoopGroup() {
            return new KQueueEventLoopGroup();
        }

        @Override
        public EventLoopGroup newLoopGroup(int nThreads) {
            return new KQueueEventLoopGroup(nThreads);
        }

        @Override
        public boolean isAvailable() {
            return KQueue.isAvailable();
        }
    };

    @Getter private Class<? extends Channel> channelClass;
    @Getter private Class<? extends ServerChannel> serverChannelClass;
    @Getter private Class<? extends DatagramChannel> datagramChannelClass;

    ChannelSystem(Class<? extends Channel> channelClass, Class<? extends ServerChannel> serverChannelClass, Class<? extends DatagramChannel> datagramChannelClass) {
        this.channelClass = channelClass;
        this.serverChannelClass = serverChannelClass;
        this.datagramChannelClass = datagramChannelClass;
    }

    public abstract EventLoopGroup newLoopGroup();
    public abstract EventLoopGroup newLoopGroup(int nThreads);

    public ServerBootstrap newServer() {
        return apply(new ServerBootstrap());
    }

    public Bootstrap newClient() {
        return apply(new Bootstrap());
    }

    public ServerBootstrap apply(ServerBootstrap bootstrap) {
        return bootstrap.group(newLoopGroup()).channel(serverChannelClass);
    }

    public Bootstrap apply(Bootstrap bootstrap) {
        return bootstrap.group(newLoopGroup()).channel(channelClass);
    }

    public abstract boolean isAvailable();

    private static ChannelSystem optimal;

    public static ChannelSystem getOptimal() {
        if (optimal == null) {
            if (EPOLL.isAvailable()) {
                optimal = EPOLL;
            } else if (K_QUEUNE.isAvailable()) {
                optimal = K_QUEUNE;
            } else {
                optimal = NIO;
            }
        }
        return optimal;
    }

}
