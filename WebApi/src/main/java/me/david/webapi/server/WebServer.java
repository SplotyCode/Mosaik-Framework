package me.david.webapi.server;

public interface WebServer {

    void listen(int port);

    void shutdown();

    boolean isRunning();

    void installErrorFactory();
    void uninstallErrorFactory();

}
