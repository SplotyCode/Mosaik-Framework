package papa;

import de.splotycode.davidlib.startup.Main;
import me.david.davidlib.annotation.Disabled;
import me.david.davidlib.application.Application;
import me.david.davidlib.startup.BootContext;
import me.david.webapi.WebApplicationType;
import me.david.webapi.server.NettyWebServer;

@Disabled
public class WebApp extends Application implements WebApplicationType {

    public static void main(String[] args) {
        Main.main(args);
    }

    @Override
    public void start(BootContext context) {
        setWebServer(new NettyWebServer(this));
        listen(60006);
    }

    @Override
    public String getName() {
        return "Example";
    }
}
