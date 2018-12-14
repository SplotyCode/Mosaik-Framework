package me.david.webapi.server.kaisa;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class KaisaAcceptThread extends Thread {

    private KaisaServer server;

    public KaisaAcceptThread(KaisaServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        while (server.isRunning()) {
            SocketChannel connection = null;
            try {
                connection = server.getSocket().accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Handle Full queue's
            server.getWaiting().offer(connection);
            server.getExecutorFeedThread().updateQueune();
        }
        notify();
    }

    public synchronized void awaitShutdown() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
