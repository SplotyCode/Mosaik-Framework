package io.github.splotycode.mosaik.networking.statistics.remote;

import io.github.splotycode.mosaik.networking.cloudkit.CloudKit;
import io.github.splotycode.mosaik.networking.component.INetworkProcess;
import io.github.splotycode.mosaik.networking.packet.serialized.PacketSerializer;
import io.github.splotycode.mosaik.networking.service.Service;
import io.github.splotycode.mosaik.networking.statistics.HostStatisticListener;
import io.github.splotycode.mosaik.networking.statistics.HostStatistics;
import io.github.splotycode.mosaik.networking.statistics.ServiceStatistics;
import io.github.splotycode.mosaik.networking.statistics.component.StatisticalHost;
import io.github.splotycode.mosaik.util.cache.Cache;
import io.github.splotycode.mosaik.util.cache.LazyLoad;
import io.github.splotycode.mosaik.util.collection.LevelIterable;
import io.github.splotycode.mosaik.util.logger.Logger;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.*;
import java.util.function.Consumer;

@EqualsAndHashCode(callSuper = true)
public class RemoteHostStatistics extends AbstractRemoteStatistics implements HostStatistics {

    private static final Logger LOGGER = Logger.getInstance(RemoteHostStatistics.class);

    @Getter private StatisticalHost host;
    private CloudKit cloudKit;

    private double cpu;
    private long freeRam;
    private Map<String, RemoteServiceStatistics> services = new HashMap<>();

    private final LevelIterable<INetworkProcess> instanceIterable = new LevelIterable<INetworkProcess>(services.values())
            .on(RemoteServiceStatistics.class, (context, element) -> {
                context.addAll(element.instances.values());
                return false;
            });

    private Cache<Integer> totalConnections = new LazyLoad<Integer>() {
        @Override
        protected Integer initialize() {
            int connections = 0;
            for (RemoteServiceStatistics statistics : services.values()) {
                connections += statistics.totalConnections();
            }
            return connections;
        }
    };
    private Cache<Integer> totalInstances = new LazyLoad<Integer>() {
        @Override
        protected Integer initialize() {
            int instances = 0;
            for (RemoteServiceStatistics statistics : services.values()) {
                instances += statistics.totalInstances();
            }
            return instances;
        }
    };

    public RemoteHostStatistics(StatisticalHost host, CloudKit cloudKit) {
        this.host = host;
        this.cloudKit = cloudKit;
    }

    @Override
    public Collection<INetworkProcess> getInstances(Service service) {
        return getInstances(service.displayName());
    }

    @Override
    public Collection<INetworkProcess> getInstances(String service) {
        return services.get(service).getInstances();
    }

    @Override
    public boolean hasService(Service service) {
        return hasService(service.displayName());
    }

    @Override
    public boolean hasService(String service) {
        return services.containsKey(service);
    }

    @Override
    public Set<String> getServices() {
        return services.keySet();
    }

    @Override
    public void forEachService(Consumer<ServiceStatistics> consumer) {
        services.values().forEach(consumer);
    }

    @Override
    public ServiceStatistics getService(Service service) {
        return getService(service.displayName());
    }

    @Override
    public ServiceStatistics getService(String service) {
        return services.get(service);
    }

    @Override
    public Iterable<INetworkProcess> getInstances() {
        return instanceIterable;
    }

    @Override
    public int totalInstances(Service service) {
        return totalInstances(Objects.requireNonNull(service, "service").displayName());
    }

    @Override
    public int totalInstances(String service) {
        RemoteServiceStatistics statistics = services.get(service);
        return statistics == null ? 0 : statistics.totalInstances();
    }

    @Override
    public int totalConnections(Service service) {
        return totalConnections(Objects.requireNonNull(service, "service").displayName());
    }

    @Override
    public int totalConnections(String service) {
        RemoteServiceStatistics statistics = services.get(service);
        return statistics == null ? 0 : statistics.totalConnections();
    }

    @Override
    public int totalServices() {
        return services.size();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, ServiceStatistics> serviceMap() {
        return (Map<String, ServiceStatistics>) (Map<String, ?>) services;
    }

    @Override
    public int totalInstances() {
        return totalInstances.getValue();
    }

    @Override
    public double getCPULoad() {
        return cpu;
    }

    @Override
    public long getFreeMemory() {
        return freeRam;
    }

    @Override
    public int totalConnections() {
        return totalConnections.getValue();
    }

    @Override
    public synchronized void read(PacketSerializer packet) throws Exception {
        freeRam = packet.readLong();
        cpu = packet.readDouble();

        int connectionSize = packet.readVarInt();
        for (int i = 0; i < connectionSize; i++) {
            String name = packet.readString();
            RemoteServiceStatistics statistics = services.get(name);
            if (statistics == null) {
                statistics = new RemoteServiceStatistics(host, name);
                services.put(name, statistics);
            }
            statistics.read(packet);
        }

       triggerUpdate();
    }

    @Override
    public void triggerUpdate() {
        totalConnections.clear();
        totalInstances.clear();

        super.triggerUpdate();

        LOGGER.debug("Updated host " + host.address() + ": " + this);

        cloudKit.getHandler().call(HostStatisticListener.class, listener -> listener.update(host));
    }

    @Override
    public String toString() {
        return "RemoteHostStatistics{" +
                "cpu=" + cpu +
                ", freeRam=" + freeRam +
                ", services=" + getServices() +
                '}';
    }
}
