package me.david.webapi.server;

import me.david.davidlib.util.reflection.classregister.IListClassRegister;
import me.david.webapi.WebApplicationType;
import me.david.webapi.response.error.ErrorFactory;
import me.david.webapi.session.SessionSystem;

import java.util.Collection;

public interface WebServer {

    WebApplicationType getApplication();

    void listen(int port);

    void shutdown();

    boolean isRunning();

    void installErrorFactory(ErrorFactory factory);
    void uninstallErrorFactory(ErrorFactory factory);

    IListClassRegister<SessionSystem> getSessionLoader();

    Collection<SessionSystem> getSessionSystems();

}
