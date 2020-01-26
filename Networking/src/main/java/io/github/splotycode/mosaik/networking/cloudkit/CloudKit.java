package io.github.splotycode.mosaik.networking.cloudkit;

import io.github.splotycode.mosaik.networking.config.ConfigKey;
import io.github.splotycode.mosaik.networking.config.ConfigProvider;
import io.github.splotycode.mosaik.networking.config.SimpleConfigProvider;
import io.github.splotycode.mosaik.networking.config.StaticConfigProvider;
import io.github.splotycode.mosaik.networking.host.Host;
import io.github.splotycode.mosaik.networking.host.SelfHost;
import io.github.splotycode.mosaik.networking.master.MasterService;
import io.github.splotycode.mosaik.networking.master.host.RemoteMasterHost;
import io.github.splotycode.mosaik.networking.service.Service;
import io.github.splotycode.mosaik.networking.statistics.CloudStatistics;
import io.github.splotycode.mosaik.networking.statistics.component.StatisticalHost;
import io.github.splotycode.mosaik.networking.util.IpFilterHandler;
import io.github.splotycode.mosaik.networking.util.IpResolver;
import io.github.splotycode.mosaik.networking.util.MosaikAddress;
import io.github.splotycode.mosaik.util.collection.FilteredCollection;
import io.github.splotycode.mosaik.util.listener.Listener;
import io.github.splotycode.mosaik.util.listener.MultipleListenerHandler;
import io.github.splotycode.mosaik.util.task.TaskExecutor;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.Collection;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CloudKit {

    public static Builder build() {
        return new CloudKit().new Builder();
    }

    private ConfigProvider configProvider;

    private MultipleListenerHandler<Listener> handler = new MultipleListenerHandler<>();

    private final ServiceManager serviceManager = new ServiceManager(this);

    private TaskExecutor localTaskExecutor;

    private HostMapProvider hosts;
    private HostProvider hostProvider;

    private IpResolver localIpResolver;
    private Host selfHost;
    private SelfHostProvider selfHostProvider;

    private IpFilterHandler ipFilter = new IpFilterHandler() {
        @Override
        protected Collection<MosaikAddress> grantedAddresses() {
            return hosts.hosts.keySet();
        }
    };

    private CloudStatistics statistics = new CloudStatistics(this);
    @SuppressWarnings("unchecked")
    private Collection<StatisticalHost> statisticalHosts = (Collection<StatisticalHost>) (Collection<?>)
            new FilteredCollection<>(getHosts(), host -> host instanceof StatisticalHost);

    @Deprecated
    public CloudKit startMasterService(long updateDelay, int port) {
        startService(new MasterService(updateDelay, port));
        return this;
    }

    @Deprecated
    public CloudKit startMasterService() {
        startService(new MasterService(this));
        return this;
    }

    public CloudKit setConfigProvider(ConfigProvider configProvider) {
        if (configProvider instanceof Service) {
            startService((Service) configProvider);
        } else {
            setConfigProvider0(configProvider);
        }
        return this;
    }

    public CloudKit stopService(Service service) {
        handler.removeListener(service);
        serviceManager.stop(service);
        return this;
    }

    public TreeMap<MosaikAddress, Host> hostMap() {
        return hosts.getValue();
    }

    public Host getHostByAddress(MosaikAddress address) {
        return hostMap().get(address);
    }

    public Collection<Host> getHosts() {
        return hostMap().values();
    }

    public MosaikAddress selfAddress() {
        return localIpResolver.getIpAddress();
    }

    public CloudKit startService(Service service) {
        serviceManager.start(service, () -> handler.addListener(service));

        if (service instanceof ConfigProvider) {
            setConfigProvider0((ConfigProvider) service);
        }
        return this;
    }

    private void setConfigProvider0(ConfigProvider configProvider) {
        handler.call(ConfigProviderChangeListener.class, listener -> listener.newConfigProvider(this.configProvider, configProvider));
        this.configProvider = configProvider;
    }

    private void checkConfigProvider() {
        if (configProvider == null) throw new ServiceNotAvailableException("config provider");
    }

    public <T> CloudKit setConfig(ConfigKey<T> key, T value) {
        checkConfigProvider();
        configProvider.setConfigValue(key, value);
        return this;
    }

    public CloudKit setConfig(String key, Object value) {
        checkConfigProvider();
        configProvider.set(key, value);
        return this;
    }

    public Collection<Service> getServices() {
        return serviceManager.getServiceCollection();
    }

    public <S extends Service> S getServiceByClass(Class<S> clazz) {
        return serviceManager.getServiceByClass(clazz);
    }

    public <S extends Service> S requireServiceByClass(Class<S> clazz) {
        return serviceManager.requireServiceByClass(clazz);
    }

    public Service getServiceByName(String name) {
        return serviceManager.getServiceByName(name);
    }

    public Service requireServiceByName(String name) {
        return serviceManager.requireServiceByName(name);
    }

    public <T> T getConfig(ConfigKey<T> key) {
        checkConfigProvider();
        return configProvider.getValue(key);
    }

    public MosaikAddress localAddress() {
        return localIpResolver.getIpAddress();
    }

    public class Builder {

        public Builder localIpResolver(IpResolver resolver) {
            localIpResolver = resolver;
            return this;
        }

        public Builder localIpResolver(String preferred, String... failover) {
            localIpResolver = IpResolver.create(preferred, failover);
            return this;
        }

        public Builder localIpResolver(boolean local) {
            localIpResolver = local ? IpResolver.createLocal() : IpResolver.createDefaults();
            return this;
        }

        public Builder localIpResolver() {
            return localIpResolver(false);
        }

        public Builder localTaskExecutor(int nThreads) {
            return localTaskExecutor(nThreads, Executors.defaultThreadFactory());
        }

        public Builder localTaskExecutor(int nThreads, ThreadFactory factory) {
            return localTaskExecutor(new TaskExecutor(nThreads == 1 ?
                    Executors.newSingleThreadExecutor(factory) :
                    Executors.newFixedThreadPool(nThreads, factory)));
        }

        public Builder localTaskExecutor(ExecutorService executor) {
            localTaskExecutor = new TaskExecutor(executor);
            return this;
        }

        public Builder localTaskExecutor(TaskExecutor taskExecutor) {
            localTaskExecutor = taskExecutor;
            return this;
        }

        public Builder hostProvider(HostProvider provider) {
            hostProvider = provider;
            return this;
        }

        public Builder selfHostProvider(SelfHostProvider provider) {
            selfHostProvider = provider;
            return this;
        }

        public ConfigBuilder toConfig() {
            build();
            return new ConfigBuilder(this);
        }

        public CloudKit configAndBuild() {
            return toConfig().finish();
        }

        public CloudKit build() {
            if (localIpResolver == null) {
                localIpResolver = IpResolver.GLOBAL;
            }
            if (localTaskExecutor == null) {
                localTaskExecutor(2);
            }
            if (hostProvider == null) {
                hostProvider = RemoteMasterHost.PROVIDER;
            }
            if (selfHostProvider == null) {
                selfHostProvider = SelfHost.PROVIDER;
            }
            selfHost = selfHostProvider.provide(CloudKit.this);
            return CloudKit.this;
        }

    }

    @AllArgsConstructor
    public class ConfigBuilder {

        private Builder mainBuilder;

        public ConfigBuilder hostMapProvider(HostMapProvider provider) {
            hosts = provider;
            return this;
        }

        public ConfigBuilder hostMapProviderConfig(String path) {
            checkConfigProvider();
            hosts = new ConfigHostMapProvider(CloudKit.this, path);
            return this;
        }

        public ConfigBuilder configProvider(ConfigProvider provider) {
            setConfigProvider(provider);
            return this;
        }

        public ConfigBuilder fileConfig(File file) {
            setConfigProvider(new StaticConfigProvider(file));
            return this;
        }

        protected void checkConfigProvider() {
            if (configProvider == null) {
                configProvider = new SimpleConfigProvider();
            }
        }

        public CloudKit finish() {
            apply();
            return CloudKit.this;
        }

        private void apply() {
            checkConfigProvider();
        }

        public Builder applyAndBack() {
            apply();
            return back();
        }

        public Builder back() {
            return mainBuilder;
        }

    }
}
