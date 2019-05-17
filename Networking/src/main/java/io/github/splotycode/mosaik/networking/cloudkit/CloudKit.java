package io.github.splotycode.mosaik.networking.cloudkit;

import io.github.splotycode.mosaik.networking.config.ConfigKey;
import io.github.splotycode.mosaik.networking.config.ConfigProvider;
import io.github.splotycode.mosaik.networking.host.AddressChangeListener;
import io.github.splotycode.mosaik.networking.host.Host;
import io.github.splotycode.mosaik.networking.host.SelfHost;
import io.github.splotycode.mosaik.networking.master.MasterHost;
import io.github.splotycode.mosaik.networking.master.MasterService;
import io.github.splotycode.mosaik.networking.service.Service;
import io.github.splotycode.mosaik.networking.util.IpResolver;
import io.github.splotycode.mosaik.networking.util.MosaikAddress;
import io.github.splotycode.mosaik.util.task.TaskExecutor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CloudKit implements AddressChangeListener {

    public static CloudKit.SetUp create() {
        return new CloudKit().new SetUp();
    }

    private ConfigProvider configProvider;
    private final HashSet<Service> services = new HashSet<>();

    private TaskExecutor localTaskExecutor;

    private String hostConfigName;
    private final TreeMap<MosaikAddress, Host> hosts = new TreeMap<>();
    private HostProvider hostProvider;

    private IpResolver localIpResolver;
    private Host selfHost;
    private SelfHostProvider selfHostProvider;

    public CloudKit startMasterService(long updateDelay, int port) {
        startService(new MasterService(this, updateDelay, port));
        return this;
    }

    public CloudKit startMasterService() {
        startService(new MasterService(this));
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
                    Host hostObj = hostProvider.provide(this, host.toString());
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

    private void checkConfigProvider() {
        if (configProvider == null) throw new ServiceNotAvailableExcpetion("config provider");
    }

    public <T> CloudKit setConfig(ConfigKey<T> key, T value) {
        checkConfigProvider();
        configProvider.setConfigValue(key, value);
        return this;
    }

    public <T> T getConfig(ConfigKey<T> key) {
        checkConfigProvider();
        return configProvider.getConfigValue(key);
    }

    public class SetUp {

        public SetUp localIpResolver(IpResolver resolver) {
            localIpResolver = resolver;
            return this;
        }

        public SetUp localIpResolver(String preferred, String... failover) {
            localIpResolver = IpResolver.create(preferred, failover);
            return this;
        }

        public SetUp localIpResolver(boolean local) {
            localIpResolver = local ? IpResolver.createLocal() : IpResolver.createDefaults();
            return this;
        }

        public SetUp localIpResolver() {
            return localIpResolver(false);
        }

        public SetUp localTaskExecutor(int nThreads) {
            return localTaskExecutor(nThreads, Executors.defaultThreadFactory());
        }

        public SetUp localTaskExecutor(int nThreads, ThreadFactory factory) {
            return localTaskExecutor(new TaskExecutor(nThreads == 1 ?
                    Executors.newSingleThreadExecutor(factory) :
                    Executors.newFixedThreadPool(nThreads, factory)));
        }

        public SetUp localTaskExecutor(ExecutorService executor) {
            localTaskExecutor = new TaskExecutor(executor);
            return this;
        }

        public SetUp localTaskExecutor(TaskExecutor taskExecutor) {
            localTaskExecutor = taskExecutor;
            return this;
        }

        public SetUp hostProvider(HostProvider provider) {
            hostProvider = provider;
            return this;
        }

        public SetUp selfHostProvider(SelfHostProvider provider) {
            selfHostProvider = provider;
            return this;
        }

        public CloudKit next() {
            if (localIpResolver == null) {
                localIpResolver = IpResolver.GLOBAL;
            }
            if (localTaskExecutor == null) {
                localTaskExecutor(2);
            }
            if (hostProvider == null) {
                hostProvider = MasterHost.PROVIDER;
            }
            if (selfHostProvider == null) {
                selfHostProvider = SelfHost.PROVIDER;
            }
            selfHost = selfHostProvider.provide(CloudKit.this);
            hosts.put(selfHost.address(), selfHost);
            return CloudKit.this;
        }

    }
}
