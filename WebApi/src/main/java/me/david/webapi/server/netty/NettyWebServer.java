package me.david.webapi.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import me.david.webapi.WebApplicationType;
import me.david.webapi.server.AbstractWebServer;
import me.david.webapi.server.WebServer;

public class NettyWebServer extends AbstractWebServer implements WebServer {

    private ChannelFuture channel;
    private EventLoopGroup loopGroup;
    private NettyThread thread = new NettyThread();

    private WebServerHandler handler = new WebServerHandler(this);

    public NettyWebServer(WebApplicationType application) {
        super(application);
    }

    @Override
    public void listen(int port) {
        super.listen(port);
        thread.start();
    }

    @Override
    public void shutdown() {
        loopGroup.shutdownGracefully();
        try {
            channel.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isRunning() {
        return loopGroup != null && !loopGroup.isShutdown() && !loopGroup.isTerminated() && !loopGroup.isShuttingDown();
    }

    public class NettyThread extends Thread {

        @Override
        public void run() {
            loopGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
            try {
                channel = new ServerBootstrap()
                        .option(ChannelOption.SO_BACKLOG, 1024)
                        .option(ChannelOption.SO_REUSEADDR, true)
                        .group(loopGroup)
                        .channel(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel channel) throws Exception {
                                final ChannelPipeline p = channel.pipeline();
                                p.addLast(new HttpRequestDecoder());
                                p.addLast("aggregator", new HttpObjectAggregator(512*1024));
                                p.addLast(new HttpResponseEncoder());
                                p.addLast(new HttpContentCompressor());
                                p.addLast("handler", handler);
                            }
                        })
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
