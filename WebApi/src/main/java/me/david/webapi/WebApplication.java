package me.david.webapi;

import com.google.common.reflect.ClassPath;
import lombok.Getter;
import me.david.webapi.handler.HandlerManager;
import me.david.webapi.handler.anotation.parameter.ParameterResolver;
import me.david.webapi.server.WebServer;

import java.io.IOException;

@Deprecated
public class WebApplication {

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
            System.out.println("Registered tranformer: " + parameterResolver.getClass().getSimpleName());
        }
    }

    public void registerTransformer(String clazz) {
        try {
            registerTransformer((Class<? extends ParameterResolver>) Class.forName(clazz));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void registerTransformers(String packagePath) {
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
    }

}
