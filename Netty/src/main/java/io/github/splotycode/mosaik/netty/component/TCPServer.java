package io.github.splotycode.mosaik.netty.component;

import io.github.splotycode.mosaik.netty.component.listener.BindListener;
import io.github.splotycode.mosaik.netty.component.listener.BoundListener;
import io.github.splotycode.mosaik.netty.component.listener.ChannelListener;
import io.github.splotycode.mosaik.netty.component.listener.UnBoundListener;
import io.github.splotycode.mosaik.netty.exception.SecureExcpetion;
import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.listener.MultipleListenerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.OptionalSslHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.net.ssl.SSLException;
import java.io.File;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings({"unused", "WeakerAccess"})
public class TCPServer<S extends TCPServer<S>> {

    protected Supplier<Integer> port;
    protected String host;
    protected SocketAddress address;

    protected int nThreads = -1;

    protected SSLMode sslMode;
    protected SslContext sslContext;

    protected boolean logging;
    protected LogLevel logLevel;
    protected String logCategory;

    protected Map<ChannelOption, Object> channelOptions;
    protected Map<ChannelOption, Object> childOptions;

    protected boolean applyDefault = true;

    protected ChannelSystem channelSystem;
    protected EventLoopGroup loopGroup;
    protected ServerBootstrap bootstrap;
    protected Class<? extends ServerChannel> channelClass;

    protected ChannelHandler costomHandler;
    protected ChannelHandler childHandler;

    protected MultipleListenerHandler handler = new MultipleListenerHandler();

    protected ChannelFuture channel;
    protected AtomicBoolean running = new AtomicBoolean(false);
    protected Lock bindLock = new ReentrantLock();

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
            loopGroup = nThreads == -1 ? channelSystem.newLoopGroup() : channelSystem.newLoopGroup(nThreads);
        }
        if (channelClass == null) {
            channelClass = channelSystem.getServerChannelClass();
        }

        if (bootstrap == null) {
            bootstrap = new ServerBootstrap();
        }

        if (sslMode == null) {
            sslMode = SSLMode.NONE;
        }

        if (sslMode != SSLMode.NONE && sslContext == null) {
            sslSelfSigned();
        }

        if (address != null) {
            if (port != null) throw new IllegalArgumentException("Illegal Combination: Address and Port");
            if (host != null) throw new IllegalArgumentException("Illegal Combination: Address and Host");
            return;
        }

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

        if (logging) {
            if (logLevel == null) {
                logLevel = LogLevel.DEBUG;
            }
            if (logCategory == null) {
                logCategory = "TCPServer - " + address.toString();
            }
        }
    }

    protected void configureDefaults() {
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_BACKLOG, 1000)

                .childOption(ChannelOption.AUTO_READ, false)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
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

    public S sslSelfSigned() {
        try {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            ssl(ssc.certificate(), ssc.privateKey());
        } catch (CertificateException ex) {
            throw new SecureExcpetion("Failed to set SSL Context", ex);
        }
        return self();
    }

    public S ssl(File certificate, File priavteKey) {
        try {
            sslContext = SslContextBuilder.forServer(certificate, priavteKey).build();
        } catch (SSLException ex) {
            throw new SecureExcpetion("Failed to set SSL Context", ex);
        }
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

    public S childHandler(ChannelHandler handler) {
        childHandler = handler;
        return self();
    }

    public S handler(ChannelHandler handler) {
        costomHandler = handler;
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

    public S option(Map<ChannelOption, Object> options) {
        channelOptions = options;
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

    public boolean running() {
        return running.get();
    }

    public S bindAndBlock() throws InterruptedException {
        bind();
        block();
        return self();
    }

    public S bind() throws InterruptedException {
        try {
            if (!bindLock.tryLock()) throw new IllegalStateException("Server already binding");
            if (running.get()) throw new IllegalStateException("Server is already running");

            prepareValues();
            bootstrap.group(loopGroup).channel(channelClass);

            bootstrap.handler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();
                    if (logging) {
                        pipeline.addLast(new LoggingHandler(logCategory, logLevel));
                    }
                    if (costomHandler != null) {
                        pipeline.addLast("costom", costomHandler);
                    }
                }
            });

            bootstrap.childHandler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel channel) {
                    ChannelPipeline pipeline = channel.pipeline();
                    switch (sslMode) {
                        case FORCE:
                            pipeline.addLast("ssl", new OptionalSslHandler(sslContext));
                            break;
                        case ALLOW:
                            pipeline.addLast("ssl", sslContext.newHandler(channel.alloc()));
                            break;
                    }

                    if (childHandler != null) {
                        pipeline.addLast("costom", childHandler);
                    }
                }
            });

            if (applyDefault) {
                configureDefaults();
            }

            for (Map.Entry<ChannelOption, Object> entry : channelOptions.entrySet()) {
                bootstrap.option(entry.getKey(), entry.getValue());
            }
            for (Map.Entry<ChannelOption, Object> entry : childOptions.entrySet()) {
                bootstrap.childOption(entry.getKey(), entry.getValue());
            }

            handler.call(BindListener.class, (Consumer<BindListener>) h -> h.bind(bootstrap));

            channel = bootstrap.bind(address);

            running.set(true);

            channel.channel().closeFuture().addListener(future -> {
                running.set(false);
                handler.call(UnBoundListener.class, (Consumer<UnBoundListener>) h -> h.unBound(future));
            });

            channel.sync();
            handler.call(BoundListener.class, (Consumer<BoundListener>) h -> h.bound(channel));

            return self();
        } finally {
            bindLock.unlock();
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
