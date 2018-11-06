package me.david.webapi.server;

import me.david.webapi.response.error.ErrorFactory;

public interface WebServer {

    void listen(int port);

    void shutdown();

    boolean isRunning();

    void installErrorFactory(ErrorFactory factory);
    void uninstallErrorFactory(ErrorFactory factory);

}
