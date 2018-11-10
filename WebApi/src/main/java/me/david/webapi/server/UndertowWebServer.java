package me.david.webapi.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.undertow.Undertow;
import io.undertow.util.HttpString;
import lombok.Getter;
import me.david.webapi.WebApplicationType;
import me.david.webapi.handler.HandlerManager;
import me.david.webapi.response.Response;
import me.david.webapi.response.error.ErrorFactory;
import me.david.webapi.response.error.ErrorHandler;
import org.apache.commons.io.IOUtils;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

public class UndertowWebServer implements WebServer {

    private Undertow server;

    @Getter private int request = 0;
    @Getter private long totalTime = 0;

    private ErrorHandler errorHandler = new ErrorHandler();

    private HandlerManager handlerManager;
    private WebApplicationType application;

    public UndertowWebServer(WebApplicationType webApplication) {
        application = webApplication;
        handlerManager = webApplication.getWebHandler();
    }

    @Override
    public void listen(int port) {
        System.out.println("Hey");
        server = Undertow.builder()
                .addHttpListener(port, "localhost")
                .setHandler(exchange -> {
                    try {
                        request++;
                        System.out.println(exchange.getRequestURI() + " " + exchange.getDestinationAddress().getHostString());
                        QueryStringDecoder uri = new QueryStringDecoder(exchange.getRequestURI());
                        /*Request request = new Request(
                                uri.path(),
                                exchange.getDestinationAddress().getHostName(),
                                new Method(nettyRequest.method().name()),
                                HttpUtil.isKeepAlive(nettyRequest)
                        );
                        for (Map.Entry<String, List<String>> get : uri.parameters().entrySet()) {
                            request.getGet().put(get.getKey(), get.getValue().get(0));
                        }

                        long start = System.currentTimeMillis();
                        Response response = handlerManager.handleRequest(request);
                        response.finish(request, application);
                        totalTime += System.currentTimeMillis() - start;

                        ByteBuf content = Unpooled.buffer(128);
                        content.writeBytes(response.getRawContent(), response.getRawContent().available());

                        DefaultFullHttpResponse nettyResponse = new DefaultFullHttpResponse(
                                convertHttpVersion(response.getHttpVersion()),
                                HttpResponseStatus.valueOf(response.getResponseCode()),
                                content
                        );
                        for (Map.Entry<String, String> pair : response.getHeaders().entrySet()) {
                            nettyResponse.headers().set(pair.getKey(), pair.getValue());
                        }
                        ctx.writeAndFlush(nettyResponse);*/
                    } catch (Throwable ex) {
                        Response response = errorHandler.handleError(ex);
                        response.finish(null, application);
                        exchange.setResponseCode(response.getResponseCode());
                        for (Map.Entry<String, String> pair : response.getHeaders().entrySet()) {
                            exchange.getResponseHeaders().put(HttpString.tryFromString(pair.getKey()), pair.getValue());
                        }
                        exchange.getResponseSender().send(ByteBuffer.wrap(IOUtils.toByteArray(response.getRawContent())));
                    }
                }).build();
        server.start();
    }

    @Override
    public void shutdown() {
        server.stop();
    }

    @Override
    public boolean isRunning() {
        return !server.getWorker().isShutdown();
    }

    @Override
    public void installErrorFactory(ErrorFactory factory) {
        errorHandler.installErrorFactory(factory);
    }

    @Override
    public void uninstallErrorFactory(ErrorFactory factory) {
        errorHandler.uninstallErrorFactory(factory);
    }

}
