package io.github.splotycode.mosaik.webapi.server.kaisa;

import io.github.splotycode.mosaik.webapi.WebApplicationType;
import lombok.Getter;
import lombok.Setter;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.webapi.server.AbstractWebServer;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Queue;

public class KaisaServer extends AbstractWebServer {

    @Getter private KaisaAcceptThread acceptThread;
    @Getter private KaisaExecutorFeedThread executorFeedThread;

    @Getter @Setter private Queue<SocketChannel> waiting;
    @Getter private KaisaConnectionExecutor executor;

    @Getter private DataFactory config = new DataFactory();

    @Getter private ServerSocketChannel socket;

    public KaisaServer(WebApplicationType application) {
        super(application);
    }

    @Override
    public void listen(int port) {
        super.listen(port);
        acceptThread = new KaisaAcceptThread(this);
        executorFeedThread = new KaisaExecutorFeedThread(this);
        executor = new KaisaConnectionExecutor(this);

        try {
            socket = ServerSocketChannel.open();
            socket.configureBlocking(false);
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
        executor.shutdown(true);
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
