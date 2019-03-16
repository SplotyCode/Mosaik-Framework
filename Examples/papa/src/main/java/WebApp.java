import io.github.splotycode.mosaik.runtime.startup.BootContext;

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
