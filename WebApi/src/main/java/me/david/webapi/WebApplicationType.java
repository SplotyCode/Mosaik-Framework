package me.david.webapi;

import me.david.davidlib.runtime.application.ApplicationType;
import me.david.davidlib.runtime.startup.BootContext;
import me.david.davidlib.util.datafactory.DataKey;
import me.david.davidlib.util.reflection.ClassCollector;
import me.david.davidlib.util.reflection.classregister.IListClassRegister;
import me.david.davidlib.util.reflection.classregister.ListClassRegister;
import me.david.webapi.handler.anotation.parameter.ParameterResolver;
import me.david.webapi.request.body.RequestContentHandler;
import me.david.webapi.response.error.ErrorHandler;
import me.david.webapi.server.WebServer;

import java.util.ArrayList;

public interface WebApplicationType extends ApplicationType {

    DataKey<WebServer> WEB_SERVER = new DataKey<>("web.webserver");
    DataKey<IListClassRegister<ParameterResolver>> PARAMETER_RESOLVER_REGISTER = new DataKey<>("web.param_resolve_register");
    DataKey<ErrorHandler> ERROR_HANDLER = new DataKey<>("web.error_handler");
    DataKey<IListClassRegister<RequestContentHandler>> CONTENT_HANDLER_REGISTER = new DataKey<>("web.content_handler_register");

    ClassCollector PARAMETER_RESOLVER_COLLECTOR = ClassCollector.newInstance()
                                                    .setNoDisableds(true)
                                                    .setInPackage("me.david.webapi.handler.anotation.parameter.defaultresolver")
                                                    .setNeedAssignable(ParameterResolver.class);

    ClassCollector CONTENT_HANDLER_COLLECTOR = ClassCollector.newInstance()
                                                    .setNoDisableds(true)
                                                    .setInPackage("me.david.webapi.request.body")
                                                    .setNeedAssignable(RequestContentHandler.class)
                                                    .setOnlyClasses(true);

    default void initType(BootContext context, WebApplicationType dummy) {
        getLocalShutdownManager().addShutdownTask(() -> {
            WebServer server = getWebServer();
            if (server != null && server.isRunning())
                server.shutdown();
        });
        getDataFactory().putData(PARAMETER_RESOLVER_REGISTER, new ListClassRegister<>(new ArrayList<>(), ParameterResolver.class));
        getDataFactory().putData(CONTENT_HANDLER_REGISTER, new ListClassRegister<>(new ArrayList<>(), RequestContentHandler.class));
        getDataFactory().putData(ERROR_HANDLER, new ErrorHandler());

        getContentHandlerRegister().registerAll( CONTENT_HANDLER_COLLECTOR);
        getParameterResolveRegister().registerAll(PARAMETER_RESOLVER_COLLECTOR);
        getLogger().info("Registered " + getParameterResolveRegister().getAll().size() + " default Parameter Resolvers");
    }

    default WebServer getWebServer() {
        return getData(WEB_SERVER);
    }

    default IListClassRegister<RequestContentHandler> getContentHandlerRegister() {
        return getData(CONTENT_HANDLER_REGISTER);
    }

    default ErrorHandler getErrorHandler() {
        return getData(ERROR_HANDLER);
    }


    default IListClassRegister<ParameterResolver> getParameterResolveRegister() {
        return getData(PARAMETER_RESOLVER_REGISTER);
    }

    default void listen(int port) {
        WebServer server = getWebServer();
        if (server.isRunning())
            server.shutdown();
        getLogger().info("Starting WebServer under " + port + " (" + server.getClass().getSimpleName() + ")");
        server.listen(port);
    }

    default void setWebServer(WebServer server) {
        WebServer currentServer = getWebServer();
        if (currentServer != null) currentServer.shutdown();

        getDataFactory().putData(WEB_SERVER, server);
    }

}
