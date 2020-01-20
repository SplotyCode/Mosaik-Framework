package io.github.splotycode.mosaik.webapi.server.netty;

import io.github.splotycode.mosaik.util.ExceptionUtil;
import io.github.splotycode.mosaik.webapi.WebApplicationType;
import io.github.splotycode.mosaik.webapi.config.WebConfig;
import io.github.splotycode.mosaik.webapi.server.AbstractWebServer;
import io.github.splotycode.mosaik.webapi.server.WebServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import lombok.Getter;
import lombok.Setter;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

@Getter
public class NettyWebServer extends AbstractWebServer implements WebServer {

    protected ChannelFuture channel;
    protected EventLoopGroup loopGroup;
    private NettyThread thread = new NettyThread();

   @Setter
    protected WebServerHandler handler = new WebServerHandler(this);

    @Setter
    protected SslContext sslContext = null;

    @Setter
    protected NettyChannelInitializer channelInitializer = new NettyChannelInitializer(this);

    public NettyWebServer(WebApplicationType application) {
        super(application);
    }


    public NettyWebServer() {
        super();
    }

    @Override
    public void listen(int port, boolean ssl) {
        super.listen(port, ssl);
        if (ssl) {
            try {
                SelfSignedCertificate ssc = new SelfSignedCertificate();
                sslContext = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
            } catch (CertificateException | SSLException e) {
                ExceptionUtil.throwRuntime(e);
            }
        } else {
            sslContext = null;
        }
        thread.start();
    }

    @Override
    public void shutdown(Runnable future) {
        loopGroup.shutdownGracefully().addListener(nettyFuture -> future.run());
    }

    @Override
    public void shutdown() {
        loopGroup.shutdownGracefully();
    }

    @Override
    public boolean isRunning() {
        return loopGroup != null && !loopGroup.isShutdown() && !loopGroup.isTerminated() && !loopGroup.isShuttingDown();
    }

    public class NettyThread extends Thread {

        @Override
        public void run() {
            if (getConfig().containsData(WebConfig.NETTY_THREADS)) {
                int numThreads = getConfig().getData(WebConfig.NETTY_THREADS);
                loopGroup = Epoll.isAvailable() ? new EpollEventLoopGroup(numThreads) : new NioEventLoopGroup(numThreads);
            } else {
                loopGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
            }
            try {
                channel = new ServerBootstrap()
                        .option(ChannelOption.SO_BACKLOG, 1024)
                        .option(ChannelOption.SO_REUSEADDR, true)
                        .group(loopGroup)
                        .channel(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                        .childHandler(channelInitializer)
                        .childOption(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(true))
                        .childOption(ChannelOption.SO_REUSEADDR, true)
                        .childOption(ChannelOption.SO_KEEPALIVE, true)
                        .bind(address).sync();
                channel.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                shutdown();
            }
        }
    }
}
