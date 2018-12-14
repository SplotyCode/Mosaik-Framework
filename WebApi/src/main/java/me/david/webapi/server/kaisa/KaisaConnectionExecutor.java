package me.david.webapi.server.kaisa;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static me.david.webapi.server.kaisa.KaisaConfig.*;

public class KaisaConnectionExecutor {

    private ExecutorService executor;
    private KaisaServer server;

    public KaisaConnectionExecutor(KaisaServer server) {
        this.server = server;

        //Setup Connection Executor
        if (server.getConfig().containsData(CONNECTION_EXECUTOR)) {
            if (server.getConfig().containsData(CONNECTION_EXECUTOR_PARALLEL))
                throw new InvalidConfigurationException(CONNECTION_EXECUTOR.getName() + " and "  + CONNECTION_EXECUTOR_PARALLEL + " can not be used togehter");
            executor = server.getConfig().getData(CONNECTION_EXECUTOR).get();
        } else if (server.getConfig().containsData(WAITING_QUENE_MAX_CAPACITY)){
            executor = Executors.newFixedThreadPool(server.getConfig().getData(WAITING_QUENE_MAX_CAPACITY));
        } else {
            executor = Executors.newFixedThreadPool(2);
        }
    }

    public void execute(SocketChannel connection) {
        executor.execute(() -> {

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
