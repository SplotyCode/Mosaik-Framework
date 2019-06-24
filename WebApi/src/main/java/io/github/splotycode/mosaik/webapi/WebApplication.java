package io.github.splotycode.mosaik.webapi;

import io.github.splotycode.mosaik.util.logger.Logger;
import io.github.splotycode.mosaik.util.reflection.annotation.parameter.ParameterResolver;
import io.github.splotycode.mosaik.webapi.handler.HandlerManager;
import io.github.splotycode.mosaik.webapi.server.WebServer;
import lombok.Getter;

@Deprecated
public class WebApplication {

    private static Logger logger = Logger.getInstance(WebApplication.class);

    @Getter private static WebApplication instance;

    @Getter private WebServer webServer;
    @Getter private HandlerManager manager = new HandlerManager();

    public static void start(WebApplication application) {
        instance = application;

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (instance.webServer != null) {
                instance.webServer.shutdown();
            }
        }, "WebServer Shutdown Thread"));
    }

    public static void listen(int port) {
        instance.manager.initalize();
        instance.webServer.listen(port);
    }

    public static void setWebServer(WebServer webServer) {
        if (instance.webServer != null) {
            webServer.shutdown();
        }
        instance.webServer = webServer;
    }

    public void registerTransformer(Class<? extends ParameterResolver> transformer) {
        try {
            registerTransformer(transformer.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void registerTransformer(ParameterResolver parameterResolver) {
        if (!manager.getGlobalParameterResolver().contains(parameterResolver)) {
            manager.getGlobalParameterResolver().add(parameterResolver);
            logger.info("Registered tranformer: " + parameterResolver.getClass().getSimpleName());
        }
    }

    public void registerTransformer(String clazz) {
        try {
            registerTransformer((Class<? extends ParameterResolver>) Class.forName(clazz));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*public void registerTransformers(String packagePath) {

        try {
            for (ClassPath.ClassInfo classInfo : ClassPath.from(getClass().getClassLoader()).getTopLevelClassesRecursive(packagePath)) {
                Class<?> clazz = classInfo.load();
                if (ParameterResolver.class.isAssignableFrom(clazz)) {
                    registerTransformer((Class<? extends ParameterResolver>) clazz);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

}
