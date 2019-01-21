
import io.github.splotycode.mosaik.startup.Main;
import io.github.splotycode.mosaik.runtime.application.Application;
import io.github.splotycode.mosaik.runtime.startup.BootContext;
import io.github.splotycode.mosaik.webapi.WebApplicationType;
import io.github.splotycode.mosaik.webapi.server.undertow.UndertowWebServer;

public class ExampleApp extends Application implements WebApplicationType {

    public static void main(String[] args) {
        Main.main(args);
    }

    @Override
    public void start(BootContext context) {
        getLogger().info("Starting Example App");
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
