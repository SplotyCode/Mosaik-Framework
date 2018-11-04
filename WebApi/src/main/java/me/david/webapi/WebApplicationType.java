package me.david.webapi;

import com.google.common.reflect.ClassPath;
import me.david.davidlib.annotation.Disabled;
import me.david.davidlib.application.ApplicationType;
import me.david.davidlib.application.BootContext;
import me.david.davidlib.datafactory.DataKey;
import me.david.webapi.handler.HandlerFinder;
import me.david.webapi.handler.HandlerManager;
import me.david.webapi.handler.anotation.transform.Transformer;
import me.david.webapi.server.WebServer;

import java.io.IOException;

public interface WebApplicationType extends ApplicationType {

    DataKey<WebServer> webServer = new DataKey<>("web.webserver");
    DataKey<HandlerManager> handlerManager = new DataKey<>("web.handlermanager");

    default void initType(BootContext context, WebApplicationType dummy) {
        getDataFactory().putData(handlerManager, new HandlerManager());
        getLocalShutdownManager().addShutdownTask(() -> {
            WebServer server = getData(webServer);
            if (server != null)
                server.shutdown();
        });
        registerGlobalTransformers("me.david.webapi.handler.anotation.transform.defaulttransformer");
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
        server.listen(port);
    }

    default void setWebServer(WebServer server) {
        WebServer currentServer = getWebServer();
        if (currentServer != null) currentServer.shutdown();

        getDataFactory().putData(webServer, server);
    }

    default void registerGlobalTransformer(Class<? extends Transformer> transformer) {
        try {
            registerGlobalTransformer(transformer.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    default void registerGlobalTransformer(Transformer transformer) {
        HandlerManager handler = getWebHandler();
        if (!handler.getGlobalTransformer().contains(transformer) && !transformer.getClass().isAnnotationPresent(Disabled.class)) {
            handler.getGlobalTransformer().add(transformer);
            System.out.println("Registered global Tranformer: " + transformer.getClass().getSimpleName());
        }
    }

    default void registerGlobalTransformer(String clazz) {
        try {
            registerGlobalTransformer((Class<? extends Transformer>) Class.forName(clazz));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    default void registerGlobalTransformers(String packagePath) {
        try {
            for (ClassPath.ClassInfo classInfo : ClassPath.from(getClass().getClassLoader()).getTopLevelClassesRecursive(packagePath)) {
                Class<?> clazz = classInfo.load();
                if (Transformer.class.isAssignableFrom(clazz)) {
                    registerGlobalTransformer((Class<? extends Transformer>) clazz);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
