package me.david.webapi;

import me.david.davidlib.application.ApplicationType;
import me.david.davidlib.datafactory.DataKey;
import me.david.davidlib.startup.BootContext;
import me.david.davidlib.utils.reflection.classregister.IListClassRegister;
import me.david.webapi.handler.anotation.parameter.ParameterResolver;
import me.david.webapi.response.error.ErrorHandler;
import me.david.webapi.server.WebServer;

public interface WebApplicationType extends ApplicationType {

    DataKey<WebServer> WEB_SERVER = new DataKey<>("web.webserver");
    DataKey<IListClassRegister<ParameterResolver>> PARAMETER_RESOLVER_REGISTER = new DataKey<>("web.param_resolve_register");
    DataKey<ErrorHandler> ERROR_HANDLER = new DataKey<>("web.error_handler");


    default void initType(BootContext context, WebApplicationType dummy) {
        getLocalShutdownManager().addShutdownTask(() -> {
            WebServer server = getData(WEB_SERVER);
            if (server != null && server.isRunning())
                server.shutdown();
        });
        getParameterResolvRegister().registerPackage("me.david.webapi.handler.anotation.parameter.defaultresolver");
    }

    default WebServer getWebServer() {
        return getData(WEB_SERVER);
    }

    default ErrorHandler getErrorHandler() {
        return getData(ERROR_HANDLER);
    }


    default IListClassRegister<ParameterResolver> getParameterResolvRegister() {
        return getData(PARAMETER_RESOLVER_REGISTER);
    }

    default void listen(int port) {
        WebServer server = getWebServer();
        if (server.isRunning())
            server.shutdown();
        System.out.println("Starting WebServer under " + port + " (" + server.getClass().getSimpleName() + ")");
        server.listen(port);
    }

    default void setWebServer(WebServer server) {
        WebServer currentServer = getWebServer();
        if (currentServer != null) currentServer.shutdown();

        getDataFactory().putData(WEB_SERVER, server);
    }

}
