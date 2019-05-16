package io.github.splotycode.mosaik.networking.cloudkit;

import io.github.splotycode.mosaik.networking.config.ConfigKey;
import io.github.splotycode.mosaik.networking.config.ConfigProvider;
import io.github.splotycode.mosaik.networking.host.AddressChangeListener;
import io.github.splotycode.mosaik.networking.host.Host;
import io.github.splotycode.mosaik.networking.host.SelfHost;
import io.github.splotycode.mosaik.networking.master.MasterService;
import io.github.splotycode.mosaik.networking.service.Service;
import io.github.splotycode.mosaik.networking.util.IpResolver;
import io.github.splotycode.mosaik.networking.util.MosaikAddress;
import io.github.splotycode.mosaik.util.task.TaskExecutor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.TreeMap;
import java.util.concurrent.Executors;

@Getter
@Setter
public class CloudKit implements AddressChangeListener {

    private ConfigProvider configProvider;
    private final HashSet<Service> services = new HashSet<>();
    private IpResolver localIpResolver;
    private TaskExecutor localTaskExecutor;
    private final TreeMap<MosaikAddress, Host> hosts = new TreeMap<>();
    private Host selfHost = new SelfHost(localIpResolver);
    private String hostConfigName;
    private HostProvider hostProvider;

    {
        hosts.put(localIpResolver.getIpAddress(), selfHost);
    }

    public CloudKit startMasterService(long updateDelay, int port) {
        startService(new MasterService(updateDelay, localTaskExecutor, port, ipResolver, hosts));
        return this;
    }

    public CloudKit startMasterService() {
        startService(new MasterService(this, localTaskExecutor, ipResolver, hosts));
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
                hosts.put(localIpResolver.getIpAddress(), selfHost);
                for (Object host : entry.getValue(Iterable.class)) {
                    Host hostObj = hostProvider.provide(host.toString());
                    hostObj.handler().addListener(this);
                    hosts.put(hostObj.address(), hostObj);
                }
            });
        }
        return this;
    }

    @Override
    public void onChange(MosaikAddress oldAddress, MosaikAddress newAddress) {
        Host host = hosts.remove(oldAddress);
        hosts.put(newAddress, host);
    }

    public <T> T getConfig(ConfigKey<T> key) {
        if (configProvider == null) throw new ServiceNotAvailableExcpetion("config provider");
        return configProvider.getConfigValue(key.getName(), key.getType(), key.getDef());
    }
}
