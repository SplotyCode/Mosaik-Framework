package io.github.splotycode.mosaik.networking.statistics;

import com.sun.management.OperatingSystemMXBean;
import io.github.splotycode.mosaik.networking.cloudkit.CloudKit;
import io.github.splotycode.mosaik.networking.packet.serialized.PacketSerializer;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import io.github.splotycode.mosaik.networking.service.Service;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.lang.management.ManagementFactory;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class HostStatistics implements SerializedPacket {

    public static double getCpuLoad() {
        return ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getSystemCpuLoad() * 100;
    }

    public static HostStatistics current(CloudKit kit) {
        double cpu = getCpuLoad();
        long freeRam = Runtime.getRuntime().freeMemory();

        HostStatistics statistics = new HostStatistics(cpu, freeRam, (StatisticalHost) kit.getSelfHost());

        for (Service service : kit.getServices()) {
            if (service instanceof StatisticService) {
                ServiceStatistics serviceStatistics = ((StatisticService) service).statistics();
                if (serviceStatistics != null) {
                    statistics.connections.put(service.displayName(), serviceStatistics);
                }
            }
        }
        return statistics;
    }

    private HostStatistics(double cpu, long freeRam, StatisticalHost host) {
        this(host, cpu, freeRam, new HashMap<>());
    }

    public Collection<Instance> getConnection(Service service) {
        return connections.get(service.displayName()).instances.values();
    }

    private StatisticalHost host;

    private double cpu;
    private long freeRam;
    private Map<String, ServiceStatistics> connections;

    public Set<String> getServices() {
        return connections.keySet();
    }

    public int getInstances(String service) {
        ServiceStatistics statistics = connections.get(service);
        return statistics == null ? 0 : statistics.getInstancesCount();
    }

    public ServiceStatistics getService(Service service) {
        return getService(service.displayName());
    }

    public ServiceStatistics getService(String service) {
        return connections.get(service);
    }

    public int getInstances(Service service) {
        return getInstances(service.displayName());
    }

    public int getConnections(String service) {
        ServiceStatistics statistics = connections.get(service);
        return statistics == null ? 0 : statistics.totalConnections();
    }

    public int getConnections(Service service) {
        return getConnections(service.displayName());
    }

    public int totalInstances() {
        int instances = 0;
        for (ServiceStatistics statistics : connections.values()) {
            instances += statistics.getInstancesCount();
        }
        return instances;
    }

    public int totalConnections() {
        int cons = 0;
        for (ServiceStatistics statistics : connections.values()) {
            cons += statistics.totalConnections();
        }
        return cons;
    }

    @Override
    public void read(PacketSerializer packet) throws Exception {
        freeRam = packet.readLong();
        cpu = packet.readDouble();

        int connectionSize = packet.readVarInt();
        connections = new HashMap<>(connectionSize, 1);
        for (int i = 0; i < connectionSize; i++) {
            String name = packet.readString();
            ServiceStatistics statistics = new ServiceStatistics(host, name);
            statistics.read(packet);
            connections.put(name, statistics);
        }
    }

    @Override
    public void write(PacketSerializer packet) throws Exception {
        packet.writeLong(freeRam);
        packet.writeDouble(cpu);

        packet.writeVarInt(connections.size());
        for (Map.Entry<String, ServiceStatistics> service : connections.entrySet()) {
            packet.writeString(service.getKey());
            service.getValue().write(packet);
        }
    }

}
