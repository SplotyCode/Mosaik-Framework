package io.github.splotycode.mosaik.netty.component;

import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.kqueue.KQueue;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.channel.kqueue.KQueueSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public enum ChannelSystem {

    NIO(NioSocketChannel.class, NioServerSocketChannel.class) {
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
    EPOLL(EpollSocketChannel.class, EpollServerSocketChannel.class) {
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
    K_QUEUNE(KQueueSocketChannel.class, KQueueServerSocketChannel.class) {
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

    private Class<? extends Channel> channelClass;
    private Class<? extends ServerChannel> serverChannelClass;

    ChannelSystem(Class<? extends Channel> channelClass, Class<? extends ServerChannel> serverChannelClass) {
        this.channelClass = channelClass;
        this.serverChannelClass = serverChannelClass;
    }

    public abstract EventLoopGroup newLoopGroup();
    public abstract EventLoopGroup newLoopGroup(int nThreads);

    public abstract boolean isAvailable();

    public Class<? extends Channel> getChannelClass() {
        return channelClass;
    }

    public Class<? extends ServerChannel> getServerChannelClass() {
        return serverChannelClass;
    }

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
