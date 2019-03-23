package io.github.splotycode.mosaik.netty.component;

import io.github.splotycode.mosaik.netty.component.listener.BindHandler;
import io.github.splotycode.mosaik.netty.component.listener.BoundHandler;
import io.github.splotycode.mosaik.netty.component.listener.UnBoundHandler;
import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.listener.MultipleListenerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TCPServer<S extends TCPServer<S>> {

    private Supplier<Integer> port;
    private String host;
    private int rawPort;
    private SocketAddress address;

    private Map<ChannelOption, Object> channelOptions;
    private Map<ChannelOption, Object> childOptions;

    private ChannelSystem channelSystem;
    private EventLoopGroup loopGroup;
    private ServerBootstrap bootstrap;
    private Class<? extends ServerChannel> channelClass;

    private MultipleListenerHandler handler = new MultipleListenerHandler();

    private ChannelFuture channel;

    public static TCPServer create() {
        return new TCPServer();
    }

    protected S self() {
        return (S) this;
    }

    protected void prepareValues() {
        if (channelSystem == null) {
            channelSystem = ChannelSystem.getOptimal();
        }

        if (loopGroup == null) {
            loopGroup = channelSystem.newLoopGroup();
        }
        if (channelClass == null) {
            channelClass = channelSystem.getServerChannelClass();
        }

        if (bootstrap == null) {
            bootstrap = new ServerBootstrap();
        }

        if (address != null) {
            if (port != null) throw new IllegalArgumentException("Illegal Combination: Address and Port");
            if (host != null) throw new IllegalArgumentException("Illegal Combination: Address and Host");
            return;
        }

        if (port == null) {
            throw new NullPointerException("port");
        }
        rawPort = port.get();
        if (rawPort < 0) throw new IllegalArgumentException("Negative Port");

        if (StringUtil.isEmpty(host)) {
            address = new InetSocketAddress(rawPort);
        } else {
            address = new InetSocketAddress(host, rawPort);
        }
    }

    public S port(int port) {
        return port(() -> port);
    }

    public S port(Supplier<Integer> port) {
        this.port = port;
        return self();
    }

    public S host(String host) {
        this.host = host;
        return self();
    }

    public S channelSystem(ChannelSystem channelSystem) {
        this.channelSystem = channelSystem;
        return self();
    }

    public S bootstrap(ServerBootstrap bootstrap) {
        this.bootstrap = bootstrap;
        return self();
    }

    public S loopGroup(EventLoopGroup loopGroup) {
        this.loopGroup = loopGroup;
        return self();
    }

    public S shutdown() {
        loopGroup.shutdownGracefully();
        return self();
    }

    public <O> S option(ChannelOption<O> option, O value) {
        if (channelOptions == null) channelOptions = new HashMap<>();
        channelOptions.put(option, value);
        return self();
    }

    public <O> S childOption(Map<ChannelOption, Object> options) {
        childOptions = options;
        return self();
    }

    public <O> S option(Map<ChannelOption, Object> options) {
        channelOptions = options;
        return self();
    }

    public S bindAndBlock() throws InterruptedException {
        bind();
        block();
        return self();
    }

    public S bind() throws InterruptedException {
        try {
            prepareValues();
            bootstrap
                    .group(loopGroup)
                    .channel(channelClass);

            for (Map.Entry<ChannelOption, Object> entry : channelOptions.entrySet()) {
                bootstrap.option(entry.getKey(), entry.getValue());
            }
            for (Map.Entry<ChannelOption, Object> entry : childOptions.entrySet()) {
                bootstrap.childOption(entry.getKey(), entry.getValue());
            }

            handler.call(BindHandler.class, (Consumer<BindHandler>) h -> h.bind(bootstrap));

            channel = bootstrap.bind(address);

            channel.channel().closeFuture().addListener(future -> handler.call(UnBoundHandler.class, (Consumer<UnBoundHandler>) h -> h.unBound(future)));

            channel.sync();
            handler.call(BoundHandler.class, (Consumer<BoundHandler>) h -> h.bound(channel));

            return self();
        } finally {
            shutdown();
        }
    }

    public void block() throws InterruptedException {
        try {
            channel.channel().closeFuture().sync();
        } finally {
            shutdown();
        }
    }


}
