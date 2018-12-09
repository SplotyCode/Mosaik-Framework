package me.david.webapi.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.*;
import lombok.Getter;
import me.david.webapi.WebApplicationType;
import me.david.webapi.handler.HandlerManager;
import me.david.webapi.request.Method;
import me.david.webapi.request.Request;
import me.david.webapi.response.Response;
import me.david.webapi.response.error.ErrorFactory;
import me.david.webapi.response.error.ErrorHandler;

import java.net.InetSocketAddress;
import java.util.Map;

public class NettyWebServer extends Thread implements WebServer {

    private ChannelFuture channel;
    private EventLoopGroup loopGroup;

    private WebServerHandler handler = new WebServerHandler();
    private ErrorHandler errorHandler = new ErrorHandler();

    private HandlerManager handlerManager;
    private WebApplicationType application;

    @Getter private int request = 0;
    @Getter private long totalTime = 0;

    public NettyWebServer(WebApplicationType webApplication) {
        application = webApplication;
        handlerManager = webApplication.getWebHandler();
    }

    private InetSocketAddress address;

    @Override
    public void listen(int port) {
        address = new InetSocketAddress(port);
        start();
    }

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

    @Override
    public void shutdown() {
        loopGroup.shutdownGracefully();
        try {
            channel.channel().closeFuture().sync();
        } catch (InterruptedException e) { }
    }

    @Override
    public boolean isRunning() {
        return loopGroup != null && !loopGroup.isShutdown() && !loopGroup.isTerminated() && !loopGroup.isShuttingDown();
    }

    @Override
    public void installErrorFactory(ErrorFactory factory) {
        errorHandler.installErrorFactory(factory);
    }

    @Override
    public void uninstallErrorFactory(ErrorFactory factory) {
        errorHandler.uninstallErrorFactory(factory);
    }

    @ChannelHandler.Sharable
    private class WebServerHandler extends SimpleChannelInboundHandler {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg instanceof FullHttpRequest) {
                request++;
                FullHttpRequest nettyRequest = (FullHttpRequest) msg;
                if (nettyRequest.decoderResult() != DecoderResult.SUCCESS) {
                    ctx.close();
                    return;
                }
                QueryStringDecoder uri = new QueryStringDecoder(nettyRequest.uri());
                Request request = new Request(
                        uri.path(),
                        transformIpAddress(ctx.channel().remoteAddress().toString()),
                        new Method(nettyRequest.method().name()),
                        HttpUtil.isKeepAlive(nettyRequest),
                        nettyRequest.content().array()
                );
                request.setGet(uri.parameters());

                long start = System.currentTimeMillis();
                Response response = handlerManager.handleRequest(request);
                response.finish(request, application);
                totalTime += System.currentTimeMillis() - start;

                ByteBuf content = Unpooled.buffer(response.getRawContent().available());
                content.writeBytes(response.getRawContent(), response.getRawContent().available());

                DefaultFullHttpResponse nettyResponse = new DefaultFullHttpResponse(
                        convertHttpVersion(response.getHttpVersion()),
                        HttpResponseStatus.valueOf(response.getResponseCode()),
                        content
                );
                for (Map.Entry<String, String> pair : response.getHeaders().entrySet()) {
                    nettyResponse.headers().set(pair.getKey(), pair.getValue());
                }
                ctx.writeAndFlush(nettyResponse);
            } else {
                super.channelRead(ctx, msg);
            }
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            Response response = errorHandler.handleError(cause);
            response.finish(null, application);
            ByteBuf byteBuf = Unpooled.buffer();
            byteBuf.writeBytes(response.getRawContent(), response.getRawContent().available());
            DefaultFullHttpResponse nettyResponse = new DefaultFullHttpResponse(
                    convertHttpVersion(response.getHttpVersion()),
                    HttpResponseStatus.valueOf(response.getResponseCode()),
                    byteBuf
            );
            for (Map.Entry<String, String> pair : response.getHeaders().entrySet()) {
                nettyResponse.headers().set(pair.getKey(), pair.getValue());
            }
            ctx.writeAndFlush(nettyResponse);
        }
    }

    private HttpVersion convertHttpVersion(me.david.webapi.response.HttpVersion version) {
        switch (version) {
            case VERSION_1_0:
                return HttpVersion.HTTP_1_0;
            case VERSION_1_1:
                return HttpVersion.HTTP_1_1;
            default:
                throw new IllegalStateException(version.name() + " Http Version is not supported by netty");
        }
    }

    private String transformIpAddress(String address) {
        //Normal Netty Ip Addresses: /127.0.0.1:52480
        return address.substring(1).split(":")[0];
    }
}
