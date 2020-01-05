package io.github.splotycode.mosaik.networking.component;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.*;
import io.netty.channel.kqueue.*;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

@Getter
public enum ChannelSystem {

    EPOLL(EpollSocketChannel.class, EpollServerSocketChannel.class, EpollDatagramChannel.class, true) {
        @Override
        public EventLoopGroup newLoopGroup() {
            return new EpollEventLoopGroup();
        }

        @Override
        public EventLoopGroup newLoopGroup(int nThreads) {
            return new EpollEventLoopGroup(nThreads);
        }

        @Override
        public EventLoopGroup newLoopGroup(int nThreads, ThreadFactory threadFactory) {
            return new EpollEventLoopGroup(nThreads, threadFactory);
        }

        @Override
        public EventLoopGroup newLoopGroup(int nThreads, Executor executor) {
            return new EpollEventLoopGroup(nThreads, executor);
        }

        @Override
        public boolean isAvailable() {
            return Epoll.isAvailable();
        }
    },
    K_QUEUE(KQueueSocketChannel.class, KQueueServerSocketChannel.class, KQueueDatagramChannel.class, true) {
        @Override
        public EventLoopGroup newLoopGroup() {
            return new KQueueEventLoopGroup();
        }

        @Override
        public EventLoopGroup newLoopGroup(int nThreads) {
            return new KQueueEventLoopGroup(nThreads);
        }

        @Override
        public EventLoopGroup newLoopGroup(int nThreads, ThreadFactory threadFactory) {
            return new KQueueEventLoopGroup(nThreads, threadFactory);
        }

        @Override
        public EventLoopGroup newLoopGroup(int nThreads, Executor executor) {
            return new KQueueEventLoopGroup(nThreads, executor);
        }

        @Override
        public boolean isAvailable() {
            return KQueue.isAvailable();
        }
    },
    NIO(NioSocketChannel.class, NioServerSocketChannel.class, NioDatagramChannel.class, true) {
        @Override
        public EventLoopGroup newLoopGroup() {
            return new NioEventLoopGroup();
        }

        @Override
        public EventLoopGroup newLoopGroup(int nThreads) {
            return new NioEventLoopGroup(nThreads);
        }

        @Override
        public EventLoopGroup newLoopGroup(int nThreads, ThreadFactory threadFactory) {
            return new NioEventLoopGroup(nThreads, threadFactory);
        }

        @Override
        public EventLoopGroup newLoopGroup(int nThreads, Executor executor) {
            return new NioEventLoopGroup(nThreads, executor);
        }

        @Override
        public boolean isAvailable() {
            return true;
        }
    },
    LOCAL(LocalChannel.class, LocalServerChannel.class, null, false) {
        @Override
        public EventLoopGroup newLoopGroup() {
            return new DefaultEventLoopGroup();
        }

        @Override
        public EventLoopGroup newLoopGroup(int nThreads) {
            return new DefaultEventLoopGroup(nThreads);
        }

        @Override
        public EventLoopGroup newLoopGroup(int nThreads, ThreadFactory threadFactory) {
            return new DefaultEventLoopGroup(nThreads, threadFactory);
        }

        @Override
        public EventLoopGroup newLoopGroup(int nThreads, Executor executor) {
            return new DefaultEventLoopGroup(nThreads, executor);
        }

        @Override
        public boolean isAvailable() {
            return true;
        }
    };

    private Class<? extends Channel> channelClass;
    private Class<? extends ServerChannel> serverChannelClass;
    private Class<? extends DatagramChannel> datagramChannelClass;

    private boolean remote;

    ChannelSystem(Class<? extends Channel> channelClass, Class<? extends ServerChannel> serverChannelClass,
                  Class<? extends DatagramChannel> datagramChannelClass, boolean remote) {
        this.channelClass = channelClass;
        this.serverChannelClass = serverChannelClass;
        this.datagramChannelClass = datagramChannelClass;
        this.remote = remote;
    }

    public abstract boolean isAvailable();

    public abstract EventLoopGroup newLoopGroup();
    public abstract EventLoopGroup newLoopGroup(int nThreads);
    public abstract EventLoopGroup newLoopGroup(int nThreads, ThreadFactory threadFactory);
    public abstract EventLoopGroup newLoopGroup(int nThreads, Executor executor);

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

    private static ChannelSystem optimal;

    public static ChannelSystem getOptimal() {
        if (optimal == null) {
            for (ChannelSystem channelSystem : values()) {
                if (channelSystem.isAvailable() && channelSystem.isRemote()) {
                    optimal = channelSystem;
                    break;
                }
            }
        }
        return optimal;
    }

}
