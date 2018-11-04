import me.david.davidlib.Main;
import me.david.davidlib.application.Application;
import me.david.davidlib.application.BootContext;
import me.david.davidlib.datafactory.DataFactory;
import me.david.webapi.WebApplicationType;
import me.david.webapi.server.NettyWebServer;

public class ExampleApp extends Application implements WebApplicationType {

    public static void main(String[] args) {
        Main.main(args);
    }

    @Override
    public void start(BootContext context) {
        System.out.println("Starte Example App");
        setWebServer(new NettyWebServer(this));
        listen(60006);
    }

    @Override
    public void configurise(DataFactory config) {

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
