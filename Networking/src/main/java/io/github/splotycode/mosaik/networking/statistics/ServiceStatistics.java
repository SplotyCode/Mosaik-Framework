package io.github.splotycode.mosaik.networking.statistics;

import io.github.splotycode.mosaik.networking.component.NetworkComponent;
import io.github.splotycode.mosaik.networking.packet.serialized.PacketSerializer;
import io.github.splotycode.mosaik.networking.util.CurrentConnectionHandler;

import java.util.HashMap;
import java.util.Map;

public class ServiceStatistics {

    private Map<Integer, Integer> connections = new HashMap<>();

    public void read(PacketSerializer serializer) {
        int instancesSize = serializer.readVarInt();
        connections = new HashMap<>(instancesSize, 1);
        for (int i2 = 0; i2 < instancesSize; i2++) {
            connections.put(serializer.readVarInt(), serializer.readVarInt());
        }
    }

    public void write(PacketSerializer serializer) {
        serializer.writeVarInt(getInstances());
        for (Map.Entry<Integer, Integer> instance : connections.entrySet()) {
            serializer.writeVarInt(instance.getKey());
            serializer.writeVarInt(instance.getValue());
        }
    }

    public int getInstances() {
        return connections.size();
    }

    public int totalConnections() {
        int counter = 0;
        for (int instanceConnections : connections.values()) {
            counter += instanceConnections;
        }
        return counter;
    }

    public ServiceStatistics addComponent(NetworkComponent<?, ?, ?> component) {
        if (component.running()) {
            CurrentConnectionHandler handler = component.getHandler(CurrentConnectionHandler.class);
            connections.put(component.port(), (int) handler.getConnections());
        }
        return this;
    }

}
