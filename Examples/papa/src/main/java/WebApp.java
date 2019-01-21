package papa;

import io.github.splotycode.mosaik.startup.Main;
import io.github.splotycode.mosaik.annotations.Disabled;
import io.github.splotycode.mosaik.runtime.application.Application;
import io.github.splotycode.mosaik.runtime.startup.BootContext;
import io.github.splotycode.mosaik.webapi.WebApplicationType;
import io.github.splotycode.mosaik.webapi.server.netty.NettyWebServer;

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
