package io.github.splotycode.mosaik.netty.component;

import io.github.splotycode.mosaik.netty.component.listener.BindListener;
import io.github.splotycode.mosaik.netty.component.listener.BoundListener;
import io.github.splotycode.mosaik.netty.component.listener.ChannelListener;
import io.github.splotycode.mosaik.netty.component.listener.UnBoundListener;
import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.listener.MultipleListenerHandler;
import io.netty.bootstrap.AbstractBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.OptionalSslHandler;
import io.netty.handler.ssl.SslContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings({"unused", "WeakerAccess"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class NetworkComponent<B extends AbstractBootstrap<B, ? extends Channel>, C extends Channel, S extends NetworkComponent<B, C, S>> {

    protected Supplier<Integer> port;
    protected String host;
    protected SocketAddress address;

    protected int nThreads = -1;

    protected SSLMode sslMode;
    protected SslContext sslContext;

    protected boolean logging;
    protected LogLevel logLevel;
    protected String logCategory;

    protected boolean applyDefault = true;
    protected ChannelSystem channelSystem;

    protected boolean udp;

    protected EventLoopGroup loopGroup;
    protected B bootstrap;
    protected Class<? extends C> channelClass;

    protected MultipleListenerHandler handler = new MultipleListenerHandler();

    protected ChannelFuture channel;
    protected AtomicBoolean running = new AtomicBoolean(false);
    protected ReentrantLock bindLock = new ReentrantLock();

    protected Map<ChannelOption, Object> channelOptions;
    protected ChannelHandler costomHandler;

    protected void prepareValues() {
        if (channelSystem == null) {
            channelSystem = ChannelSystem.getOptimal();
        }

        if (loopGroup == null) {
            loopGroup = nThreads == -1 ? channelSystem.newLoopGroup() : channelSystem.newLoopGroup(nThreads);
        }

        if (sslMode == null) {
            sslMode = SSLMode.NONE;
        }

        if (address != null) {
            if (port != null) throw new IllegalArgumentException("Illegal Combination: Address and Port");
            if (host != null) throw new IllegalArgumentException("Illegal Combination: Address and Host");
            return;
        }

        if (!udp) {
            if (port == null) {
                throw new NullPointerException("port");
            }
            int rawPort = port.get();
            if (rawPort < 0) throw new IllegalArgumentException("Negative Port");

            if (StringUtil.isEmpty(host)) {
                address = new InetSocketAddress(rawPort);
            } else {
                address = new InetSocketAddress(host, rawPort);
            }
        }

        if (logging) {
            if (logLevel == null) {
                logLevel = LogLevel.DEBUG;
            }
            if (logCategory == null) {
                logCategory = "TCPServer - " + address.toString();
            }
        }
    }

    protected S self() {
        return (S) this;
    }

    public S bootstrap(B bootstrap) {
        this.bootstrap = bootstrap;
        return self();
    }

    public S noSSL() {
        sslMode = SSLMode.NONE;
        sslContext = null;
        return self();
    }

    public S ssl(SSLMode sslMode) {
        this.sslMode = sslMode;
        return self();
    }

    public boolean ssl() {
        return sslMode != SSLMode.NONE;
    }

    public SslContext sslContext() {
        return sslContext;
    }

    public S logging() {
        return logging(true);
    }

    public S logging(boolean logging) {
        this.logging = logging;
        return self();
    }

    public S logging(String category) {
        logging();
        logCategory = category;
        return self();
    }

    public S logging(String category, LogLevel logLevel) {
        logging();
        this.logLevel = logLevel;
        return self();
    }

    public S noLogging() {
        this.logging = false;
        logCategory = null;
        logLevel = null;
        return self();
    }

    public S nThreads(int nThreads) {
        this.nThreads = nThreads;
        return self();
    }

    public S port(int port) {
        return port(() -> port);
    }

    public S applyDefaults(boolean apply) {
        applyDefault = apply;
        return self();
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

    public S loopGroup(EventLoopGroup loopGroup) {
        this.loopGroup = loopGroup;
        return self();
    }

    public S shutdown() {
        loopGroup.shutdownGracefully();
        return self();
    }

    public S onBind(BindListener listener) {
        handler.addListener(listener);
        return self();
    }

    public S onBound(BoundListener listener) {
        handler.addListener(listener);
        return self();
    }

    public S onUnBound(UnBoundListener listener) {
        handler.addListener(listener);
        return self();
    }

    public S onChannelStatus(ChannelListener listener) {
        handler.addListener(listener);
        return self();
    }

    public S handler(ChannelHandler handler) {
        costomHandler = handler;
        return self();
    }

    public <O> S option(ChannelOption<O> option, O value) {
        if (channelOptions == null) channelOptions = new HashMap<>();
        channelOptions.put(option, value);
        return self();
    }

    public S option(Map<ChannelOption, Object> options) {
        channelOptions = options;
        return self();
    }

    public boolean running() {
        return running.get();
    }

    public void block() throws InterruptedException {
        try {
            channel.channel().closeFuture().sync();
        } finally {
            shutdown();
        }
    }

    protected ChannelInboundHandler getSSLHandler(Channel channel) {
        switch (sslMode) {
            case FORCE:
                return new OptionalSslHandler(sslContext);
            case ALLOW:
                return sslContext.newHandler(channel.alloc());
        }
        return null;
    }

    public final S bind() throws InterruptedException {
        try {
            if (!bindLock.tryLock()) throw new IllegalStateException("Server already binding");
            if (running.get()) throw new IllegalStateException("Server is already running");

            prepareValues();
            bootstrap.group(loopGroup);
            applyChannel();

            handlerLogic();

            if (applyDefault) {
                configureDefaults();
            }

            optionLogic();

            handler.call(BindListener.class, (Consumer<BindListener>) h -> h.bind(bootstrap));

            channel = doBind();

            running.set(true);

            channel.channel().closeFuture().addListener(future -> {
                running.set(false);
                handler.call(UnBoundListener.class, (Consumer<UnBoundListener>) h -> h.unBound(future));
            });

            channel.sync();
            handler.call(BoundListener.class, (Consumer<BoundListener>) h -> h.bound(channel));
            return self();
        } finally {
            shutdown();
            bindLock.unlock();
        }
    }

    protected abstract void applyChannel();
    protected abstract ChannelFuture doBind();
    protected abstract void doHandlers(ChannelPipeline pipeline);

    protected void optionLogic() {
        for (Map.Entry<ChannelOption, Object> entry : channelOptions.entrySet()) {
            bootstrap.option(entry.getKey(), entry.getValue());
        }
    }

    protected void handlerLogic() {
        bootstrap.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                ChannelPipeline pipeline = channel.pipeline();
                if (logging) {
                    pipeline.addLast(new LoggingHandler(logCategory, logLevel));
                }
                doHandlers(pipeline);
                if (costomHandler != null) {
                    pipeline.addLast("costom", costomHandler);
                }
            }
        });
    }

    protected void configureDefaults() {
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
    }

    public S bindAndBlock() throws InterruptedException {
        bind();
        block();
        return self();
    }


}
