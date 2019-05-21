import io.github.splotycode.mosaik.networking.cloudkit.CloudKit;
import io.github.splotycode.mosaik.networking.master.MasterHost;
import io.github.splotycode.mosaik.networking.master.MasterSelfHost;
import io.github.splotycode.mosaik.networking.master.MasterService;
import io.github.splotycode.mosaik.runtime.startup.StartUpInvoke;

import java.util.Collections;

public class Test {

    public static void main(String[] args) {
        StartUpInvoke.invokeTestSuite();
        CloudKit kit =
                CloudKit.build()
                        .localIpResolver(false)
                        .hostProvider(MasterHost.PROVIDER)
                        .selfHostProvider(MasterSelfHost.PROVIDER)
                .toConfig()
                        .hostMapProviderConfig("roots")
                .finish();
        kit.setConfig(MasterService.PORT, 8008);
        kit.setConfig("roots", Collections.emptyList());
        kit.startMasterService();
        //NetClient client = new NetClient();
        //    TCPServer server = TCPServer.build().port(333)
        //            .onUnBound(new ReconnectListener(1.2f, 1, 2))
        //            .bind(false);
        /*ChannelFuture future = TCPServer.build().onBind((component, bootstrap) -> System.out.println("BIND!"))
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
