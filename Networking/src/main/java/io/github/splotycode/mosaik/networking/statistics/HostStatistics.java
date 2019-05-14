package io.github.splotycode.mosaik.networking.statistics;

import io.github.splotycode.mosaik.networking.service.Service;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class HostStatistics {

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
