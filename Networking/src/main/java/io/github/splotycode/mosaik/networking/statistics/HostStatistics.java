package io.github.splotycode.mosaik.networking.statistics;

import com.sun.management.OperatingSystemMXBean;
import io.github.splotycode.mosaik.networking.cloudkit.CloudKit;
import io.github.splotycode.mosaik.networking.component.NetworkComponent;
import io.github.splotycode.mosaik.networking.service.ManagedComponentService;
import io.github.splotycode.mosaik.networking.service.Service;
import io.github.splotycode.mosaik.networking.service.SingleComponentService;
import io.github.splotycode.mosaik.networking.util.CurrentConnectionHandler;
import io.github.splotycode.mosaik.util.Pair;
import io.github.splotycode.mosaik.util.collection.CollectionUtil;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class HostStatistics {

    private static Map<Integer, Integer> getCurrentConnections(Service service) {
        if (service instanceof CostomStatisticService) {
            return ((CostomStatisticService) service).statistics();
        }
        if (service instanceof SingleComponentService) {
            NetworkComponent<?, ? ,?> component = ((SingleComponentService) service).component();
            if (component != null && component.running()) {
                CurrentConnectionHandler handler = component.getHandler(CurrentConnectionHandler.class);
                return CollectionUtil.newHashMap(new Pair<>(component.port(), (int) handler.getConnections()));
            }
        }
        if (service instanceof ManagedComponentService) {
            Map<Integer, Integer> connections = new HashMap<>();
            for (NetworkComponent<?, ?, ?> component : ((ManagedComponentService) service).getInstances()) {
                if (!component.running()) continue;
                CurrentConnectionHandler handler = component.getHandler(CurrentConnectionHandler.class);
                connections.put(component.port(), (int) handler.getConnections());
            }
            return connections;
        }
        return null;
    }

    public static int getCpuLoad() {
        return  (int) Math.round(((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getSystemCpuLoad());
    }

    public static HostStatistics current(CloudKit kit) {
        int cpu = getCpuLoad();
        long freeRam = Runtime.getRuntime().freeMemory();
        Map<String, Map<Integer, Integer>> connections = new HashMap<>();
        for (Service service : kit.getServices()) {
            Map<Integer, Integer> serviceConnections = getCurrentConnections(service);
            if (serviceConnections != null) {
                connections.put(service.displayName(), serviceConnections);
            }
        }
        return new HostStatistics(cpu, freeRam, connections);
    }

    private final int cpu;
    private final long freeRam;
    private final Map<String, Map<Integer, Integer>> connections;

    public Set<String> getServices() {
        return connections.keySet();
    }

    public int getInstances(String service) {
        Map<Integer, Integer> data = connections.get(service);
        return data == null ? 0 : data.size();
    }

    public int getInstances(Service service) {
        return getInstances(service.displayName());
    }

    public int getConnections(String service) {
        Map<Integer, Integer> data = connections.get(service);
        if (data == null) return 0;
        int connections = 0;
        for (int instanceConnections : data.values()) {
            connections += instanceConnections;
        }
        return connections;
    }

    public int getConnections(Service service) {
        return getConnections(service.displayName());
    }

    public int totalInstances() {
        int instances = 0;
        for (Map<Integer, Integer> service : connections.values()) {
            instances += service.size();
        }
        return instances;
    }

    public int totalConnections() {
        int cons = 0;
        for (Map<Integer, Integer> service : connections.values()) {
            for (int instanceConnection : service.values()) {
                cons += instanceConnection;
            }
        }
        return cons;
    }

}
