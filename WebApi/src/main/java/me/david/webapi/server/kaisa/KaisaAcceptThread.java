package me.david.webapi.server.kaisa;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;

import static me.david.webapi.server.kaisa.KaisaConfig.WAITING_QUENE_MAX_CAPACITY;
import static me.david.webapi.server.kaisa.KaisaConfig.WAITING_QUEUE_SUPPLIER;

public class KaisaAcceptThread extends Thread {

    private KaisaServer server;

    public KaisaAcceptThread(KaisaServer server) {
        this.server = server;

        //Setup Waiting Queue
        if (server.getConfig().containsData(WAITING_QUEUE_SUPPLIER)) {
            if (server.getConfig().containsData(WAITING_QUENE_MAX_CAPACITY))
                throw new InvalidConfigurationException(WAITING_QUENE_MAX_CAPACITY.getName() + " and "  + WAITING_QUEUE_SUPPLIER + " can not be used togehter");
            server.setWaiting(server.getConfig().getData(WAITING_QUEUE_SUPPLIER).get());
        } else if (server.getConfig().containsData(WAITING_QUENE_MAX_CAPACITY)){
            server.setWaiting(new ArrayBlockingQueue<>(server.getConfig().getData(WAITING_QUENE_MAX_CAPACITY)));
        } else {
            server.setWaiting(new ArrayBlockingQueue<>(1200));
        }
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
        notifyAll();
    }

    public synchronized void awaitShutdown() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
