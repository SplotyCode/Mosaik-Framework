package me.david.webapi;

import com.google.common.reflect.ClassPath;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.david.webapi.handler.HandlerManager;
import me.david.webapi.handler.anotation.transform.Transformer;
import me.david.webapi.server.WebServer;

import java.io.IOException;

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

    public void registerTransformer(Class<? extends Transformer> transformer) {
        try {
            registerTransformer(transformer.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void registerTransformer(Transformer transformer) {
        if (!manager.getGlobalTransformer().contains(transformer)) {
            manager.getGlobalTransformer().add(transformer);
            System.out.println("Registered tranformer: " + transformer.getClass().getSimpleName());
        }
    }

    public void registerTransformer(String clazz) {
        try {
            registerTransformer((Class<? extends Transformer>) Class.forName(clazz));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void registerTransformers(String packagePath) {
        try {
            for (ClassPath.ClassInfo classInfo : ClassPath.from(getClass().getClassLoader()).getTopLevelClassesRecursive(packagePath)) {
                Class<?> clazz = classInfo.load();
                if (Transformer.class.isAssignableFrom(clazz)) {
                    registerTransformer((Class<? extends Transformer>) clazz);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class Config {



    }

}
