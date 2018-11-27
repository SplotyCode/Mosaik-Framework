package me.david.webapi;

import me.david.davidlib.application.ApplicationType;
import me.david.davidlib.datafactory.DataKey;
import me.david.davidlib.startup.BootContext;
import me.david.davidlib.utils.reflection.ClassRegister;
import me.david.webapi.handler.HandlerFinder;
import me.david.webapi.handler.HandlerManager;
import me.david.webapi.handler.anotation.parameter.ParameterResolver;
import me.david.webapi.response.error.ErrorFactory;
import me.david.webapi.server.WebServer;

public interface WebApplicationType extends ApplicationType, ClassRegister<ParameterResolver> {

    DataKey<WebServer> webServer = new DataKey<>("web.webserver");
    DataKey<HandlerManager> handlerManager = new DataKey<>("web.handlermanager");

    default void initType(BootContext context, WebApplicationType dummy) {
        getDataFactory().putData(handlerManager, new HandlerManager());
        getLocalShutdownManager().addShutdownTask(() -> {
            WebServer server = getData(webServer);
            if (server != null && server.isRunning())
                server.shutdown();
        });
        registerPackage("me.david.webapi.handler.anotation.parameter.defaultresolver");
    }

    default void registerFinder(HandlerFinder finder) {
        getData(handlerManager).addFinder(finder);
    }

    default WebServer getWebServer() {
        return getData(webServer);
    }

    default HandlerManager getWebHandler() {
        return getData(handlerManager);
    }

    default void listen(int port) {
        HandlerManager handler = getWebHandler();
        if (!handler.isInitialised())
            handler.initalize();

        WebServer server = getWebServer();
        if (server.isRunning())
            server.shutdown();
        System.out.println("Starting WebServer under " + port + " (" + server.getClass().getSimpleName() + ")");
        server.listen(port);
    }

    default void installErrorFactory(ErrorFactory factory) {
        getWebServer().installErrorFactory(factory);
    }

    default void uninstallErrorFactory(ErrorFactory factory) {
        getWebServer().uninstallErrorFactory(factory);
    }

    default void setWebServer(WebServer server) {
        WebServer currentServer = getWebServer();
        if (currentServer != null) currentServer.shutdown();

        getDataFactory().putData(webServer, server);
    }

}
