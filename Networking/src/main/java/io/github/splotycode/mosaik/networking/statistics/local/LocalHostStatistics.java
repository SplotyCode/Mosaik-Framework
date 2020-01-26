package io.github.splotycode.mosaik.networking.statistics.local;

import com.sun.management.OperatingSystemMXBean;
import io.github.splotycode.mosaik.networking.cloudkit.CloudKit;
import io.github.splotycode.mosaik.networking.component.INetworkProcess;
import io.github.splotycode.mosaik.networking.host.SelfHost;
import io.github.splotycode.mosaik.networking.packet.serialized.PacketSerializer;
import io.github.splotycode.mosaik.networking.service.Service;
import io.github.splotycode.mosaik.networking.statistics.HostStatistics;
import io.github.splotycode.mosaik.networking.statistics.ServiceStatistics;
import io.github.splotycode.mosaik.networking.statistics.component.StatisticalService;
import io.github.splotycode.mosaik.networking.statistics.remote.RemoteServiceStatistics;
import io.github.splotycode.mosaik.util.collection.LevelIterable;
import io.github.splotycode.mosaik.util.collection.MappedCollection;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.management.ManagementFactory;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

@AllArgsConstructor
public class LocalHostStatistics extends AbstractLocalStatistics implements HostStatistics {

    @Getter private SelfHost host;
    private CloudKit cloudKit;

    private final LevelIterable<INetworkProcess> instanceIterable = new LevelIterable<INetworkProcess>(cloudKit.getServices())
            .on(Service.class, LevelIterable.ignoreHandler())
            .on(StatisticalService.class, (context, element) -> {
                context.addAll(element.statistics().getInstances());
                return false;
            });

    private final Collection<String> serviceNames = new MappedCollection<>(cloudKit.getServices(), Service::displayName);

    @Override
    public double getCPULoad() {
        return ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getSystemCpuLoad();
    }

    @Override
    public long getFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    @Override
    public int totalConnections() {
        int total = 0;
        for (Service service : cloudKit.getServices()) {
            if (service instanceof StatisticalService) {
                total += ((RemoteServiceStatistics) service).totalConnections();
            }
        }
        return total;
    }

    @Override
    public int totalConnections(Service service) {
        ServiceStatistics stats = getService(service);
        return stats == null ? 0 : stats.totalConnections();
    }

    @Override
    public int totalConnections(String service) {
        ServiceStatistics stats = getService(service);
        return stats == null ? 0 : stats.totalConnections();
    }

    @Override
    public int totalInstances() {
        int total = 0;
        for (Service service : cloudKit.getServices()) {
            if (service instanceof StatisticalService) {
                total += ((StatisticalService) service).statistics().totalInstances();
            }
        }
        return total;
    }

    @Override
    public int totalInstances(Service service) {
        ServiceStatistics stats = getService(service);
        return stats == null ? 0 : stats.totalInstances();
    }

    @Override
    public int totalInstances(String service) {
        ServiceStatistics stats = getService(service);
        return stats == null ? 0 : stats.totalInstances();
    }

    @Override
    public boolean hasService(Service service) {
        return cloudKit.getServiceByName(service.displayName()) == service;
    }

    @Override
    public boolean hasService(String service) {
        return cloudKit.getServiceByName(service) != null;
    }

    @Override
    public int totalServices() {
        int total = 0;
        for (Service service : cloudKit.getServices()) {
            if (service instanceof StatisticalService) {
                total++;
            }
        }
        return total;
    }

    @Override
    public Map<String, ServiceStatistics> serviceMap() {
        Map<String, ServiceStatistics> serviceMap = new HashMap<>();
        for (Service service : cloudKit.getServices()) {
            if (service instanceof StatisticalService) {
                serviceMap.put(service.displayName(), ((StatisticalService) service).statistics());
            }
        }
        return serviceMap;
    }

    @Override
    public Collection<String> getServices() {
        return serviceNames;
    }

    @Override
    public void forEachService(Consumer<ServiceStatistics> consumer) {
        for (Service service : cloudKit.getServices()) {
            if (service instanceof StatisticalService) {
                consumer.accept(((StatisticalService) service).statistics());
            }
        }
    }

    @Override
    public ServiceStatistics getService(Service service) {
        if (service instanceof StatisticalService) {
            return ((StatisticalService) service).statistics();
        }
        Objects.requireNonNull(service, "service");
        throw new IllegalArgumentException(service.displayName() +  " is not a StatisticalService");
    }

    @Override
    public ServiceStatistics getService(String serviceName) {
        Service service = cloudKit.getServiceByName(serviceName);
        if (service instanceof StatisticalService) {
            return ((StatisticalService) service).statistics();
        }
        if (service == null) {
            return null;
        }
        throw new IllegalArgumentException(service.displayName() +  " is not a StatisticalService");
    }

    @Override
    public Iterable<INetworkProcess> getInstances() {
        return instanceIterable;
    }

    @Override
    public Collection<INetworkProcess> getInstances(Service service) {
        ServiceStatistics stats = getService(service);
        return stats == null ? null : stats.getInstances();
    }

    @Override
    public Collection<INetworkProcess> getInstances(String service) {
        ServiceStatistics stats = getService(service);
        return stats == null ? null : stats.getInstances();
    }

    @Override
    public void read(PacketSerializer packet) throws Exception {
        throw new IllegalStateException("Tried to update LocalHost over network");
    }

}
