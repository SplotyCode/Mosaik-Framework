package me.david.webapi.server;

import me.david.webapi.WebApplicationType;
import me.david.webapi.response.error.ErrorFactory;

public interface WebServer {

    WebApplicationType getApplication();

    void listen(int port);

    void shutdown();

    boolean isRunning();

    void installErrorFactory(ErrorFactory factory);
    void uninstallErrorFactory(ErrorFactory factory);

}
