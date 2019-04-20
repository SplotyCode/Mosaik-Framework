import io.github.splotycode.mosaik.networking.cloudkit.CloudKit;
import io.github.splotycode.mosaik.networking.config.ConfigService;
import io.github.splotycode.mosaik.networking.host.StaticHost;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Test {

    public static void main(String[] args) {
        //StartUpInvoke.invokeTestSuite();
        CloudKit cloudKit = new CloudKit();
        cloudKit.setHostProvider(host -> {
            try {
                return new StaticHost(InetAddress.getByName(host),
                        5001, cloudKit.getLocalTaskExecutor(),
                        20 * 1000, 15 * 1000, 8, 2);
            } catch (UnknownHostException e) {
                return null;
            }
        });
        cloudKit.startService(new ConfigService(null, true, 5000, null));
        cloudKit.startMasterService(8000, 5001);
        //NetClient client = new NetClient();
        //    TCPServer server = TCPServer.create().port(333)
        //            .onUnBound(new ReconnectListener(1.2f, 1, 2))
        //            .bind(false);
        /*ChannelFuture future = TCPServer.create().onBind((component, bootstrap) -> System.out.println("BIND!"))
                .onBound((component, future1) -> System.out.println("BOUNG!"))
                .onUnBound((component, future1) -> System.out.println("UNBOUND")).port(4444).bind(false).nettyFuture();
        future.addListener(future1 -> System.out.println("raw init1"));
        ThreadUtil.sleep(1000);
        future.addListener(future1 -> System.out.println("raw init2"));
        future.channel().closeFuture().addListener(future1 -> System.out.println("raw pre close"));
        System.out.println("---REAL CLOSE---");
        future.channel().close();
        ThreadUtil.sleep(4000);
        future.channel().closeFuture().addListener(future1 -> System.out.println("raw post close"));
        ThreadUtil.sleep(2000);*/
    }
}
