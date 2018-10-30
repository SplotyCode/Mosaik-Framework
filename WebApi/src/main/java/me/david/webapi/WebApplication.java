package me.david.webapi;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.david.webapi.handler.HandlerManager;
import me.david.webapi.server.WebServer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WebApplication {

    @Getter private static WebApplication instance;

    @Getter private WebServer webServer;
    private HandlerManager manager = new HandlerManager();

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
}
