package io.github.splotycode.mosaik.networkingweb.localdebug;

import io.github.splotycode.mosaik.annotations.visibility.VisibilityLevel;
import io.github.splotycode.mosaik.networking.cloudkit.CloudKit;
import io.github.splotycode.mosaik.networking.config.ConfigKey;
import io.github.splotycode.mosaik.networking.service.Service;
import io.github.splotycode.mosaik.networking.service.ServiceStatus;
import io.github.splotycode.mosaik.webapi.config.WebConfig;
import io.github.splotycode.mosaik.webapi.handler.handlers.UnpackingHelper;
import io.github.splotycode.mosaik.webapi.server.WebServer;
import io.github.splotycode.mosaik.webapi.server.netty.NettyWebServer;
import lombok.Getter;

@Getter
public class LocalDebugService implements Service {

    public static final ConfigKey<Integer> PORT = new ConfigKey<>("debug_local.port", Integer.class);

    private WebServer webServer = new NettyWebServer();
    private CloudKit kit;
    private UnpackingHelper unpackingHelper = new UnpackingHelper("debug/local/dynamic", false, true);

    {
        webServer.getConfig().putData(WebConfig.SEARCH_ANNOTATION_HANDLERS_VISIBILITY, VisibilityLevel.NONE);
    }

    public LocalDebugService(CloudKit kit) {
        this.kit = kit;
    }

    @Override
    public void start() {
        webServer.listen(kit.getConfig(PORT), false);
    }

    @Override
    public void stop() {
        webServer.shutdown();
    }

    @Override
    public ServiceStatus getStatus() {
        return webServer.isRunning() ? ServiceStatus.RUNNING : ServiceStatus.STOPPED;
    }

    @Override
    public String statusMessage() {
        return webServer.isRunning() ? "Listening on " + kit.getConfig(PORT) : "Offline";
    }
}
