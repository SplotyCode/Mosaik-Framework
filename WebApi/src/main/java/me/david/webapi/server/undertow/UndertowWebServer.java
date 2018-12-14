package me.david.webapi.server.undertow;

import io.undertow.Undertow;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.util.HttpString;
import me.david.webapi.WebApplicationType;
import me.david.webapi.request.AbstractRequest;
import me.david.webapi.request.Method;
import me.david.webapi.request.Request;
import me.david.webapi.response.Response;
import me.david.webapi.server.AbstractWebServer;
import me.david.webapi.server.WebServer;
import org.apache.commons.io.IOUtils;

import java.util.Map;

import static me.david.webapi.server.undertow.UndertowUtils.*;

public class UndertowWebServer extends AbstractWebServer implements WebServer {

    private Undertow server;

    public UndertowWebServer(WebApplicationType application) {
        super(application);
    }


    @Override
    public void listen(int port) {
        server = Undertow.builder()
                .addHttpListener(port, "localhost")
                .setHandler(new BlockingHandler(exchange -> {
                    try {
                        exchange.startBlocking();
                        Request request = new AbstractRequest(
                                exchange.getRequestPath(),
                                exchange.getDestinationAddress().getHostString(),
                                Method.create(exchange.getRequestMethod().toString()),
                                isKeepAlive(exchange),
                                IOUtils.toByteArray(exchange.getInputStream())
                        );
                        request.setGet(exchange.getPathParameters());


                        long start = System.currentTimeMillis();
                        Response response = handleRequest(request);
                        response.finish(request, application);
                        addTotalTime(System.currentTimeMillis() - start);

                        exchange.setStatusCode(response.getResponseCode());
                        for (Map.Entry<String, String> pair : response.getHeaders().entrySet()) {
                            exchange.getResponseHeaders().put(HttpString.tryFromString(pair.getKey()), pair.getValue());
                        }
                        send(exchange, response.getRawContent());
                    } catch (Throwable ex) {
                        Response response = handleError(ex);
                        response.finish(null, application);
                        exchange.setStatusCode(response.getResponseCode());
                        for (Map.Entry<String, String> pair : response.getHeaders().entrySet()) {
                            exchange.getResponseHeaders().put(HttpString.tryFromString(pair.getKey()), pair.getValue());
                        }
                        send(exchange, response.getRawContent());
                    }
                })).build();
        server.start();
    }

    @Override
    public void shutdown() {
        server.stop();
    }

    @Override
    public boolean isRunning() {
        return server != null && server.getWorker() != null && !server.getWorker().isTerminated() && !server.getWorker().isShutdown();
    }

}
