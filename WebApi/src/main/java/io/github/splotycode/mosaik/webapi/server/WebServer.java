package io.github.splotycode.mosaik.webapi.server;

import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.reflection.classregister.IListClassRegister;
import io.github.splotycode.mosaik.webapi.WebApplicationType;
import io.github.splotycode.mosaik.webapi.response.error.ErrorFactory;
import io.github.splotycode.mosaik.webapi.session.SessionSystem;

import java.util.Collection;

public interface WebServer {

    WebApplicationType getApplication();

    default DataFactory getConfig() {
        return getApplication().getConfig();
    }

    void listen(int port, boolean ssl);

    @Deprecated
    default void listen(int port) {
        listen(port, false);
    }

    void shutdown();

    boolean isRunning();

    void installErrorFactory(ErrorFactory factory);
    void uninstallErrorFactory(ErrorFactory factory);

    IListClassRegister<SessionSystem> getSessionLoader();

    Collection<SessionSystem> getSessionSystems();

}
