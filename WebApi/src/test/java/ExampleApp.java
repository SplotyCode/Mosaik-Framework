import me.david.webapi.WebApplication;
import me.david.webapi.server.NettyWebServer;

public class ExampleApp extends WebApplication {

    public static void main(String[] args) {
        start(new ExampleApp());
        setWebServer(new NettyWebServer());
        listen(60006);
    }

}
