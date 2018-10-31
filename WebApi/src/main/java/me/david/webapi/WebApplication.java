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
    }

    public void listen(int port) {
        manager.initalize();
        webServer.listen(port);
    }

    public void setWebServer(WebServer webServer) {
        if (this.webServer != null) {
            webServer.shutdown();
        }
        this.webServer = webServer;
    }

    public void registerTransformer(Class<? extends Transformer> transformer) {
        try {
            registerTransformer(transformer.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void registerTransformer(Transformer transformer) {
        if (!manager.getGlobalTransformer().contains(transformer))
            manager.getGlobalTransformer().add(transformer);
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

}
