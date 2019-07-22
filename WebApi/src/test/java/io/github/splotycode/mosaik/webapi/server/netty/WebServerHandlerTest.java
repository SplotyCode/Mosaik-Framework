package io.github.splotycode.mosaik.webapi.server.netty;

import io.github.splotycode.mosaik.runtime.application.Application;
import io.github.splotycode.mosaik.runtime.startup.BootContext;
import io.github.splotycode.mosaik.webapi.WebApplicationType;
import io.github.splotycode.mosaik.webapi.config.WebConfig;
import io.github.splotycode.mosaik.webapi.handler.HttpHandler;
import io.github.splotycode.mosaik.webapi.request.HandleRequestException;
import io.github.splotycode.mosaik.webapi.request.Request;

public class WebServerHandlerTest {

    private static WebApp instance;

    public static class WebApp extends Application implements WebApplicationType {

        @Override
        public void start(BootContext context) throws Exception {
            NettyWebServer server = new NettyWebServer(this);
            setWebServer(server);
            server.getStaticHandlerFinder().register(new HttpHandler() {
                @Override
                public boolean valid(Request request) throws HandleRequestException {
                    return true;
                }

                @Override
                public boolean handle(Request request) throws HandleRequestException {
                    System.out.println(request.getPath());
                    return false;
                }
            });
            putConfig(WebConfig.IGNORE_NO_SSL_RECORD, true);
            putConfig(WebConfig.FORCE_HTTPS, false);
            listen(4444, true);
        }

        @Override
        public String getName() {
            return "test app";
        }
    }

}
