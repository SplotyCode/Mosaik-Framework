package io.github.splotycode.mosaik.networking.cloudkit;

import io.github.splotycode.mosaik.networking.config.ConfigProvider;
import io.github.splotycode.mosaik.networking.host.AddressChangeListener;
import io.github.splotycode.mosaik.networking.host.Host;
import io.github.splotycode.mosaik.networking.host.SelfHost;
import io.github.splotycode.mosaik.networking.master.MasterService;
import io.github.splotycode.mosaik.networking.service.Service;
import io.github.splotycode.mosaik.networking.util.IpResolver;
import io.github.splotycode.mosaik.util.task.TaskExecutor;
import io.github.splotycode.mosaik.valuetransformer.TransformerManager;
import lombok.Getter;
import lombok.Setter;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.concurrent.Executors;

@Getter
@Setter
public class CloudKit implements AddressChangeListener {

    private ConfigProvider configProvider;
    private HashSet<Service> services = new HashSet<>();
    private IpResolver ipResolver = IpResolver.GLOBAL;
    private TaskExecutor localTaskExecutor = new TaskExecutor(Executors.newFixedThreadPool(2));
    private final TreeMap<String, Host> hosts = new TreeMap<>();
    private Host selfHost = new SelfHost(ipResolver);
    private String hostConfigName;
    private HostProvider hostProvider;

    {
        hosts.put(ipResolver.getIpAddress(), selfHost);
    }

    public CloudKit startMasterService(long updateDelay, int port) {
        startService(new MasterService(updateDelay, localTaskExecutor, port, ipResolver, hosts));
        return this;
    }

    public CloudKit setConfigProvider(ConfigProvider configProvider) {
        if (configProvider instanceof Service) {
            startService((Service) configProvider);
        } else {
            this.configProvider = configProvider;
        }
        return this;
    }

    public CloudKit stopService(Service service) {
        services.remove(service);
        service.stop();
        return this;
    }

    public CloudKit startService(Service service) {
        services.add(service);
        service.start();
        if (service instanceof ConfigProvider) {
            configProvider = (ConfigProvider) service;
            configProvider.handler().addListener(hostConfigName, (originalUpdate, entry) -> {
                hosts.values().forEach(host -> host.handler().removeListener(this));
                hosts.clear();
                hosts.put(ipResolver.getIpAddress(), selfHost);
                for (Object host : entry.getValue(Iterable.class)) {
                    Host hostObj = hostProvider.provide(host.toString());
                    hostObj.handler().addListener(this);
                    hosts.put(host.toString(), hostObj);
                }
            });
        }
        return this;
    }

    @Override
    public void onChange(InetAddress oldAddress, InetAddress newAddress) {
        Host host = hosts.remove(TransformerManager.getInstance().transform(oldAddress, String.class));
        hosts.put(TransformerManager.getInstance().transform(newAddress, String.class), host);
    }
}
