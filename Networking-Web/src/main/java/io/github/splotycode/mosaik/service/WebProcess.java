package io.github.splotycode.mosaik.service;

import io.github.splotycode.mosaik.networking.component.INetworkProcess;
import io.github.splotycode.mosaik.webapi.server.WebServer;
import lombok.Getter;

import java.util.Objects;

@Getter
public class WebProcess implements INetworkProcess {

    protected WebServer webServer;

    public WebProcess(WebServer webServer) {
        setWebServer(webServer);
    }

    public void setWebServer(WebServer webServer) {
        Objects.requireNonNull(webServer, "webServer");
        this.webServer = webServer;
    }

    @Override
    public void stop() {
        webServer.shutdown();
    }

    @Override
    public int port() {
        return webServer.port();
    }

    @Override
    public boolean running() {
        return webServer.isRunning();
    }
}
