package io.github.splotycode.mosaik.webapi.server.kaisa;

import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicBoolean;

public class KaisaExecutorFeedThread extends Thread {

    private KaisaServer server;
    private AtomicBoolean shutdown = new AtomicBoolean();

    public KaisaExecutorFeedThread(KaisaServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        while (server.isRunning() && !shutdown.get()) {
            SocketChannel connection = server.getWaiting().poll();
            server.getExecutor().execute(connection);
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void shutdown() {
        shutdown.set(true);
    }

    public void updateQueune() {
        notifyAll();
    }
}
