package me.david.webapi.server;

import io.netty.handler.codec.http.*;
import io.undertow.Undertow;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderValues;
import io.undertow.util.HttpString;
import lombok.Getter;
import me.david.davidlib.prettyprint.PrettyPrint;
import me.david.webapi.WebApplicationType;
import me.david.webapi.handler.HandlerManager;
import me.david.webapi.response.Response;
import me.david.webapi.response.error.ErrorFactory;
import me.david.webapi.response.error.ErrorHandler;
import org.apache.commons.io.IOUtils;

import java.nio.ByteBuffer;
import java.util.Deque;
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
        server = Undertow.builder()
                .addHttpListener(port, "localhost")
                .setHandler(exchange -> {
                    try {
                        request++;
                        Request request = new Request(
                                exchange.getRequestPath(),
                                exchange.getDestinationAddress().getHostString(),
                                new Method(exchange.getRequestMethod().toString()),
                                isKeepAlive(exchange)
                        );
                        for (Map.Entry<String, Deque<String>> get : exchange.getPathParameters().entrySet()) {
                            request.getGet().put(get.getKey(), get.getValue().getFirst());
                        }

                        long start = System.currentTimeMillis();
                        Response response = handlerManager.handleRequest(request);
                        response.finish(request, application);
                        totalTime += System.currentTimeMillis() - start;

                        exchange.setStatusCode(response.getResponseCode());
                        for (Map.Entry<String, String> pair : response.getHeaders().entrySet()) {
                            exchange.getResponseHeaders().put(HttpString.tryFromString(pair.getKey()), pair.getValue());
                        }
                        exchange.getResponseSender().send(ByteBuffer.wrap(IOUtils.toByteArray(response.getRawContent())));
                    } catch (Throwable ex) {
                        Response response = errorHandler.handleError(ex);
                        response.finish(null, application);
                        exchange.setStatusCode(response.getResponseCode());
                        for (Map.Entry<String, String> pair : response.getHeaders().entrySet()) {
                            exchange.getResponseHeaders().put(HttpString.tryFromString(pair.getKey()), pair.getValue());
                        }
                        exchange.getResponseSender().send(ByteBuffer.wrap(IOUtils.toByteArray(response.getRawContent())));
                    }
                }).build();
        server.start();
    }

    private static boolean isKeepAlive(HttpServerExchange exchange) {
        String connection = exchange.getRequestHeaders().get("connection").getFirst();
        if (connection != null && HttpHeaderValues.CLOSE.contentEqualsIgnoreCase(connection)) {
            return false;
        } else if (toNettyVersion(exchange).isKeepAliveDefault()) {
            return !HttpHeaderValues.CLOSE.contentEqualsIgnoreCase(connection);
        } else {
            return HttpHeaderValues.KEEP_ALIVE.contentEqualsIgnoreCase(connection);
        }
    }

    private static HttpVersion toNettyVersion(HttpServerExchange exchange) {
        if (exchange.isHttp10()) {
            return HttpVersion.HTTP_1_0;
        }
        return HttpVersion.HTTP_1_1;
    }

    @Override
    public void shutdown() {
        server.stop();
    }

    @Override
    public boolean isRunning() {
        return server != null && server.getWorker() != null && !server.getWorker().isTerminated() && !server.getWorker().isShutdown();
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
