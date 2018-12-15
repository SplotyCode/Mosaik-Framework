package me.david.webapi.server.kaisa;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import me.david.webapi.response.Response;

import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static me.david.webapi.server.kaisa.KaisaConfig.*;
import static me.david.webapi.server.netty.NettyUtils.convertHttpVersion;

public class KaisaConnectionExecutor {

    private ExecutorService executor;
    private ExecutorService downloader;
    private KaisaServer server;

    public KaisaConnectionExecutor(KaisaServer server) {
        this.server = server;

        //Setup Connection Executor
        if (server.getConfig().containsData(CONNECTION_EXECUTOR)) {
            if (server.getConfig().containsData(CONNECTION_EXECUTOR_PARALLEL))
                throw new InvalidConfigurationException(CONNECTION_EXECUTOR.getName() + " and "  + CONNECTION_EXECUTOR_PARALLEL + " can not be used togehter");
            executor = server.getConfig().getData(CONNECTION_EXECUTOR).get();
        } else if (server.getConfig().containsData(CONNECTION_EXECUTOR_PARALLEL)){
            executor = Executors.newFixedThreadPool(server.getConfig().getData(CONNECTION_EXECUTOR_PARALLEL));
        } else {
            executor = Executors.newFixedThreadPool(4);
        }

        if (server.getConfig().containsData(DOWNLOAD_EXECUTOR)) {
            if (server.getConfig().containsData(DOWNLOAD_EXECUTOR_PARALLEL))
                throw new InvalidConfigurationException(DOWNLOAD_EXECUTOR.getName() + " and "  + DOWNLOAD_EXECUTOR_PARALLEL + " can not be used togehter");
            executor = server.getConfig().getData(DOWNLOAD_EXECUTOR).get();
        } else if (server.getConfig().containsData(DOWNLOAD_EXECUTOR_PARALLEL)){
            executor = Executors.newFixedThreadPool(server.getConfig().getData(DOWNLOAD_EXECUTOR_PARALLEL));
        } else {
            executor = Executors.newFixedThreadPool(2);
        }
    }

    public void execute(SocketChannel connection) {
        executor.execute(() -> {
            try {
                connection.configureBlocking(false);
                KaisaRequest request = new KaisaRequest(server, connection);
                Response response = server.handleRequest(request);
                response.finish(request, server.getApplication());
                //TODO write headers and response
            } catch (Throwable cause) {
                Response response = server.getErrorHandler().handleError(cause);
                response.finish(null, server.getApplication());
                //TODO write headers and response
            }
        });
    }

    public void shutdown(boolean force) {
        if (force) {
            executor.shutdownNow();
        } else {
            executor.shutdown();
        }
    }

}
