package io.github.splotycode.mosaik.service;

import io.github.splotycode.mosaik.networking.cloudkit.StatisticalCloudKitService;
import io.github.splotycode.mosaik.networking.component.INetworkProcess;
import io.github.splotycode.mosaik.networking.service.ServiceStatus;
import io.github.splotycode.mosaik.networking.statistics.component.SingleComponentService;
import io.github.splotycode.mosaik.webapi.server.WebServer;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

@NoArgsConstructor
public class SingleWebService extends StatisticalCloudKitService implements SingleComponentService {

    protected WebProcess process;
    private Supplier<WebServer> factory;

    public SingleWebService(Supplier<WebServer> factory) {
        this.factory = factory;
    }

    @Override
    public INetworkProcess component() {
        return process;
    }

    WebServer getWebServer() {
        if (process == null) {
            return null;
        }
        return process.getWebServer();
    }

    protected WebServer createWebServer() {
        if (factory == null) {
            throw new IllegalStateException("WebServer not specified: Specify WebServer factory or override this method");
        }
        return factory.get();
    }

    @Override
    public void start() {
        process = new WebProcess(createWebServer());
    }

    @Override
    public void stop() {
        if (process != null) {
            process.stop();
        }
    }

    @Override
    public ServiceStatus getStatus() {
        if (process == null) {
            return ServiceStatus.NOT_INITIALIZED;
        }
        return process.running() ? ServiceStatus.RUNNING : ServiceStatus.STOPPED;
    }

    @Override
    public String statusMessage() {
        if (process == null || !process.running()) {
            return "Offline";
        }
        return "Running on " + process.port();
    }
}
