package io.github.splotycode.mosaik.networking.statistics;

import io.github.splotycode.mosaik.networking.component.NetworkComponent;
import io.github.splotycode.mosaik.networking.packet.serialized.PacketSerializer;
import io.github.splotycode.mosaik.networking.util.CurrentConnectionHandler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ServiceStatistics {

    protected Map<Integer, Instance> instances = new HashMap<>();
    private StatisticalHost host;
    private String service;

    public ServiceStatistics(StatisticalHost host, String service) {
        this.host = host;
        this.service = service;
    }

    public void read(PacketSerializer serializer) {
        int instancesSize = serializer.readVarInt();
        instances = new HashMap<>(instancesSize, 1);
        for (int i2 = 0; i2 < instancesSize; i2++) {
            int port = serializer.readVarInt();
            instances.put(port, new Instance(serializer.readVarInt(), port, host, false, service));
        }
    }

    public void write(PacketSerializer serializer) {
        serializer.writeVarInt(getInstancesCount());
        for (Map.Entry<Integer, Instance> instance : instances.entrySet()) {
            serializer.writeVarInt(instance.getKey());
            serializer.writeVarInt(instance.getValue().getConnections());
        }
    }

    public int getInstancesCount() {
        return instances.size();
    }

    public Collection<Instance> getInstances() {
        return instances.values();
    }

    public Instance getLowestInstance() {
        Instance lowerst = null;
        for (Instance instance : instances.values()) {
            if (lowerst == null || lowerst.getConnections() > instance.getConnections()) {
                lowerst = instance;
            }
        }
        return lowerst;
    }

    public int totalConnections() {
        int counter = 0;
        for (Instance instance : instances.values()) {
            counter += instance.getConnections();
        }
        return counter;
    }

    public ServiceStatistics addComponent(NetworkComponent<?, ?, ?> component) {
        if (component.running()) {
            CurrentConnectionHandler handler = component.getHandler(CurrentConnectionHandler.class);
            instances.put(component.port(), new Instance((int) handler.getConnections(), component.port(), host, !component.running(), service));
        }
        return this;
    }

}
