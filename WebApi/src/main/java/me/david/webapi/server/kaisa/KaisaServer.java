package me.david.webapi.server.kaisa;

import lombok.Getter;
import me.david.davidlib.datafactory.DataFactory;
import me.david.webapi.WebApplicationType;
import me.david.webapi.server.AbstractWebServer;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;

import static me.david.webapi.server.kaisa.KaisaConfig.*;

public class KaisaServer extends AbstractWebServer {

    @Getter private KaisaAcceptThread acceptThread;
    @Getter private KaisaExecutorFeedThread executorFeedThread;

    @Getter private Queue<SocketChannel> waiting;
    @Getter private KaisaConnectionExecutor executor;

    private DataFactory config = new DataFactory();

    @Getter private ServerSocketChannel socket;

    public KaisaServer(WebApplicationType application) {
        super(application);
    }

    @Override
    public void listen(int port) {
        super.listen(port);
        acceptThread = new KaisaAcceptThread(this);
        executorFeedThread = new KaisaExecutorFeedThread(this);

        //Setup Waiting Queue
        if (config.containsData(WATING_QUEUE_SUPPLIER)) {
            if (config.containsData(WAITING_QUENE_MAX_CAPACITY))
                throw new InvalidConfigurationException(WAITING_QUENE_MAX_CAPACITY.getName() + " and "  + WATING_QUEUE_SUPPLIER + " can not be used togehter");
            waiting = config.getData(WATING_QUEUE_SUPPLIER).get();
        } else if (config.containsData(WAITING_QUENE_MAX_CAPACITY)){
            waiting = new ArrayBlockingQueue<>(config.getData(WAITING_QUENE_MAX_CAPACITY));
        } else {
            waiting = new ArrayBlockingQueue<>(1200);
        }

        try {
            socket = ServerSocketChannel.open();
            socket.bind(address);
            acceptThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void shutdown() {
        executorFeedThread.shutdown();
        acceptThread.awaitShutdown();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isRunning() {
        return socket != null && socket.socket() != null && !socket.socket().isClosed();
    }
}
