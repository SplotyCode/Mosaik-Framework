package io.github.splotycode.mosaik.networking.component;

import io.github.splotycode.mosaik.networking.component.listener.BindListener;
import io.github.splotycode.mosaik.networking.component.listener.BoundListener;
import io.github.splotycode.mosaik.networking.component.listener.UnBoundListener;
import io.github.splotycode.mosaik.networking.packet.system.PacketSystem;
import io.github.splotycode.mosaik.networking.packet.system.PacketSystemHandler;
import io.github.splotycode.mosaik.networking.util.CurrentConnectionHandler;
import io.github.splotycode.mosaik.networking.util.MosaikAddress;
import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.listener.Listener;
import io.github.splotycode.mosaik.util.listener.MultipleListenerHandler;
import io.github.splotycode.mosaik.util.logger.Logger;
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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings({"unused", "WeakerAccess"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class NetworkComponent<B extends AbstractBootstrap<B, ? extends Channel>, C extends Channel, S extends NetworkComponent<B, C, S>> implements INetworkComponent<S>, INetworkProcess {

    protected Logger logger = Logger.getInstance(getClass());

    protected Supplier<Integer> port;
    protected String host;
    protected SocketAddress address;
    protected int usedPort = -1;

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

    protected ChannelFuture channelFuture;
    protected AtomicBoolean running = new AtomicBoolean(false);
    protected AtomicBoolean ranOnce = new AtomicBoolean(false);

    protected Lock bindLock = new ReentrantLock();

    protected Map<ChannelOption, Object> channelOptions;

    protected final HandlerHolder handlers = new HandlerHolder();

    protected void newLoop() {
        loopGroup = nThreads == -1 ? channelSystem.newLoopGroup() : channelSystem.newLoopGroup(nThreads);
    }

    private String displayName;

    protected void prepareValues() {
        if (channelSystem == null) {
            channelSystem = ChannelSystem.getOptimal();
        }

        if (loopGroup == null) {
            newLoop();
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
            usedPort = port.get();
            if (usedPort < 0) throw new IllegalArgumentException("Negative Port");

            if (StringUtil.isEmpty(host)) {
                address = new InetSocketAddress(usedPort);
            } else {
                address = new InetSocketAddress(host, usedPort);
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

    public EventLoopGroup loopGroup() {
        return loopGroup;
    }

    public ChannelFuture nettyFuture() {
        return channelFuture;
    }

    @Override
    public S self() {
        return (S) this;
    }

    @Override
    public int port() {
        return usedPort;
    }

    public S address(SocketAddress address) {
        this.address = address;
        return self();
    }

    @Override
    public S noSSL() {
        sslMode = SSLMode.NONE;
        sslContext = null;
        return self();
    }

    @Override
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

    @Override
    public S logging() {
        return logging(true);
    }

    @Override
    public S logging(boolean logging) {
        this.logging = logging;
        return self();
    }

    public S logging(String category) {
        logging();
        logCategory = category;
        return self();
    }

    @Override
    public S logging(String category, LogLevel logLevel) {
        logging();
        this.logLevel = logLevel;
        return self();
    }

    @Override
    public S noLogging() {
        this.logging = false;
        logCategory = null;
        logLevel = null;
        return self();
    }

    @Override
    public S nThreads(int nThreads) {
        this.nThreads = nThreads;
        return self();
    }

    public S port(int port) {
        return port(() -> port);
    }

    @Override
    public S applyDefaults(boolean apply) {
        applyDefault = apply;
        return self();
    }

    @Override
    public S port(Supplier<Integer> port) {
        this.port = port;
        return self();
    }

    public S host(String host) {
        this.host = host;
        return self();
    }

    public S host(MosaikAddress address) {
        host = address.asString();
        return self();
    }

    @Override
    public S channelSystem(ChannelSystem channelSystem) {
        this.channelSystem = channelSystem;
        return self();
    }

    public S loopGroup(EventLoopGroup loopGroup) {
        this.loopGroup = loopGroup;
        return self();
    }

    @Override
    public String displayName() {
        return displayName == null ? getClass().getName() : displayName + " | " + getClass().getSimpleName();
    }

    @Override
    public S setDisplayName(String displayName) {
        this.displayName = displayName;
        return self();
    }

    public S shutdown() {
        if (loopGroup != null) {
            loopGroup.shutdownGracefully();
        }
        return self();
    }

    @Override
    public S onBind(BindListener listener) {
        handler.addListener(listener);
        return self();
    }

    @Override
    public S onBound(BoundListener listener) {
        handler.addListener(listener);
        return self();
    }

    @Override
    public S onUnBound(UnBoundListener listener) {
        handler.addListener(listener);
        return self();
    }

    @Override
    public S addListener(Listener listener) {
        handler.addListener(listener);
        return self();
    }

    @Override
    public S removeListener(Listener listener) {
        handler.removeListener(listener);
        return self();
    }

    @Override
    public S handler(int priority, String name, ChannelHandler handler) {
        synchronized (handlers) {
            handlers.addHandler(priority, name, handler);
        }
        return self();
    }

    @Override
    public S removeHandler(Class<? extends ChannelHandler> clazz) {
        synchronized (handlers) {
            handlers.removeHandler(clazz);
        }
        return self();
    }

    @Override
    public S removeHandler(int priority) {
        synchronized (handlers) {
            handlers.removeHandler(priority);
        }
        return self();
    }

    public <H extends ChannelHandler> H getHandler(Class<H> clazz) {
        synchronized (handlers) {
            return handlers.getHandler(clazz);
        }
    }

    public String getHandlerName(Class<? extends ChannelHandler> clazz) {
        synchronized (handlers) {
            return handlers.getHandlerName(clazz);
        }
    }

    @Override
    public <O> S option(ChannelOption<O> option, O value) {
        if (channelOptions == null) channelOptions = new HashMap<>();
        channelOptions.put(option, value);
        return self();
    }

    @Override
    public S option(Map<ChannelOption, Object> options) {
        channelOptions = options;
        return self();
    }

    @Override
    public boolean running() {
        return running.get();
    }

    public void block() throws InterruptedException {
        try {
            channelFuture.channel().closeFuture().sync();
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

    public final S bind() {
        return bind(false);
    }

    public final S bind(boolean blockOpen) {
        bindLock.lock();
        SocketAddress pastAddress = address;
        try {
            if (channelFuture != null &&
                    (!channelFuture.isDone() || !channelFuture.channel().closeFuture().isDone())) {
                throw new IllegalStateException("Server already running");
            }

            if (bootstrap != null) {
                bootstrap = null;
            }

            prepareValues();
            bootstrap.group(loopGroup);
            applyChannel();

            handlerLogic();

            if (applyDefault) {
                configureDefaults();
            }

            final SocketAddress address = this.address;

            optionLogic();

            logger.info("Binding " + displayName() + " on " + address.toString());
            handler.call(BindListener.class, (Consumer<BindListener>) h -> {
                h.bind(this, bootstrap);
            });

            channelFuture = doBind();

            running.set(true);

            channelFuture.channel().closeFuture().addListener((ChannelFutureListener) future -> {
                logger.info("UnBound " + displayName() + (future.isSuccess() ? "" : " (Failed)"));
                running.set(false);
                handler.call(UnBoundListener.class, (Consumer<UnBoundListener>) h -> h.unBound(NetworkComponent.this, future));
            });

            channelFuture.addListener(future -> {
                logger.info("Bound " + displayName() + " on " + address.toString() + (future.isSuccess() ? "" : " (Failed)"));
                handler.call(BoundListener.class, (Consumer<BoundListener>) h -> h.bound(this, channelFuture));
            });
        } finally {
            address = pastAddress;
            bindLock.unlock();
        }
        if (blockOpen) channelFuture.syncUninterruptibly();
        return self();
    }

    protected abstract void applyChannel();

    protected abstract ChannelFuture doBind();

    protected abstract void doHandlers(ChannelPipeline pipeline);

    protected void optionLogic() {
        if (channelOptions == null) return;
        for (Map.Entry<ChannelOption, Object> entry : channelOptions.entrySet()) {
            bootstrap.option(entry.getKey(), entry.getValue());
        }
    }

    protected void handlerLogic() {
        bootstrap.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) {
                ChannelPipeline pipeline = channel.pipeline();
                if (logging) {
                    pipeline.addLast(new LoggingHandler(logCategory, logLevel));
                }
                doHandlers(pipeline);
                for (HandlerHolder.AbstractHandlerData handler : handlers.getHandlerData()) {
                    pipeline.addLast(handler.getName(), handler.handler());
                }
            }

            @Override
            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                ctx.fireExceptionCaught(cause);
            }
        });
    }

    protected void configureDefaults() {
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
    }

    public S bindAndBlock() throws InterruptedException {
        bind(true);
        block();
        return self();
    }

    protected PacketSystemHandler createPacketHandler(PacketSystem system) {
        return new PacketSystemHandler(system, this instanceof IServer);
    }

    @Override
    public S usePacketSystem(int priority, PacketSystem system) {
        handler(priority, "packetSystem", createPacketHandler(system));
        return self();
    }

    @Override
    public int connectionCount() {
        CurrentConnectionHandler handler = getHandler(CurrentConnectionHandler.class);
        if (handler == null) return 0;
        return (int) handler.getConnections();
    }

    @Override
    public void stop() {
        shutdown();
    }
}
