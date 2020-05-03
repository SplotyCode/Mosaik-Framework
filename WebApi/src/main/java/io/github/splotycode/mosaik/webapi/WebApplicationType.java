package io.github.splotycode.mosaik.webapi;

import io.github.splotycode.mosaik.annotations.visibility.VisibilityLevel;
import io.github.splotycode.mosaik.runtime.application.ApplicationType;
import io.github.splotycode.mosaik.runtime.startup.BootContext;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.datafactory.DataKey;
import io.github.splotycode.mosaik.util.reflection.ClassCollector;
import io.github.splotycode.mosaik.annotationbase.context.parameter.ParameterResolver;
import io.github.splotycode.mosaik.util.reflection.classregister.IListClassRegister;
import io.github.splotycode.mosaik.util.reflection.classregister.ListClassRegister;
import io.github.splotycode.mosaik.webapi.config.WebConfig;
import io.github.splotycode.mosaik.webapi.handler.HttpHandler;
import io.github.splotycode.mosaik.webapi.request.body.RequestContentHandler;
import io.github.splotycode.mosaik.webapi.response.error.ErrorHandler;
import io.github.splotycode.mosaik.webapi.server.WebServer;

import java.util.ArrayList;

/**
 * Application type for Web Applications
 */
public interface WebApplicationType extends ApplicationType {

    DataKey<WebServer> WEB_SERVER = new DataKey<>("web.webserver");
    DataKey<IListClassRegister<ParameterResolver>> PARAMETER_RESOLVER_REGISTER = new DataKey<>("web.param_resolve_register");
    DataKey<ErrorHandler> ERROR_HANDLER = new DataKey<>("web.error_handler");
    DataKey<IListClassRegister<RequestContentHandler>> CONTENT_HANDLER_REGISTER = new DataKey<>("web.content_handler_register");

    ClassCollector PARAMETER_RESOLVER_COLLECTOR = ClassCollector.newInstance()
                                                    .setNoDisable(true)
                                                    .setVisibility(VisibilityLevel.NOT_INVISIBLE)
                                                    .setInPackage("io.github.splotycode.mosaik.webapi.handler.anotation.parameter.defaultresolver")
                                                    .setNeedAssignable(ParameterResolver.class);

    ClassCollector CONTENT_HANDLER_COLLECTOR = ClassCollector.newInstance()
                                                    .setNoDisable(true)
                                                    .setVisibility(VisibilityLevel.NOT_INVISIBLE)
                                                    .setInPackage("io.github.splotycode.mosaik.webapi.request.body")
                                                    .setNeedAssignable(RequestContentHandler.class)
                                                    .setOnlyClasses(true);

    static void setDefaults(DataFactory dataFactory) {
        dataFactory.putData(WebConfig.SEARCH_ANNOTATION_HANDLERS_VISIBILITY, VisibilityLevel.NOT_INVISIBLE);
        dataFactory.putData(WebConfig.SEARCH_HANDLERS_VISIBILITY, VisibilityLevel.ONLY_VISIBLE);
        dataFactory.putData(WebConfig.FORCE_HTTPS, true);
    }

    default void initType(BootContext context, WebApplicationType dummy) {
        setDefaults(getConfig());
        getLocalShutdownManager().addShutdownTask(() -> {
            WebServer server = getWebServer();
            if (server != null && server.isRunning())
                server.shutdown();
        });
        getDataFactory().putData(PARAMETER_RESOLVER_REGISTER, new ListClassRegister<>(new ArrayList<>(), ParameterResolver.class));
        getDataFactory().putData(CONTENT_HANDLER_REGISTER, new ListClassRegister<>(new ArrayList<>(), RequestContentHandler.class));
        getDataFactory().putData(ERROR_HANDLER, new ErrorHandler());

        getContentHandlerRegister().registerAll(CONTENT_HANDLER_COLLECTOR);
        getParameterResolveRegister().registerAll(PARAMETER_RESOLVER_COLLECTOR);
        getLogger().info("Registered " + getParameterResolveRegister().getAll().size() + " default Parameter Resolvers");
    }

    default IListClassRegister<HttpHandler> getHttpRegister() {
        return getWebServer().getHttpHandlerRegister();
    }

    default WebServer getWebServer() {
        return getData(WEB_SERVER);
    }

    default <W extends WebServer> W getWebServer(Class<? extends W> clazz) {
        WebServer server = getWebServer();
        if (clazz.isAssignableFrom(server.getClass())) {
            return (W) server;
        }
        throw new IllegalArgumentException("Can not provide WebServer of type " + clazz + " ceause it does not exsits");
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

    @Deprecated
    default void listen(int port) {
        listen(port, false);
    }

    default void listen(int port, boolean ssl) {
        WebServer server = getWebServer();
        if (server.isRunning())
            server.shutdown();
        getLogger().info("Starting WebServer under " + port + " (" + server.getClass().getSimpleName() + ")");
        server.listen(port, ssl);
    }

    default void setWebServer(WebServer server) {
        WebServer currentServer = getWebServer();
        if (currentServer != null) currentServer.shutdown();

        getDataFactory().putData(WEB_SERVER, server);
    }

}
