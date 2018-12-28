package firstapp;

import de.splotycode.davidlib.startup.Main;
import me.david.davidlib.application.Application;
import me.david.davidlib.startup.BootContext;
import me.david.webapi.WebApplicationType;
import me.david.webapi.server.undertow.UndertowWebServer;

public class ExampleApp extends Application implements WebApplicationType {

    public static void main(String[] args) {
        Main.main(args);
    }

    @Override
    public void start(BootContext context) {
        getLogger().info("Starte Example App");
        setWebServer(new UndertowWebServer(this));
        listen(60006);
    }

    @Override
    public String getName() {
        return "Example App";
    }

    /*public static void main(String[] args) {
        start(new ExampleApp());
        setWebServer(new NettyWebServer());
        listen(60006);
    }*/

}
