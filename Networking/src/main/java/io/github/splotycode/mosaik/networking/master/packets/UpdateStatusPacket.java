package io.github.splotycode.mosaik.networking.master.packets;

import io.github.splotycode.mosaik.networking.packet.serialized.PacketSerializer;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import io.github.splotycode.mosaik.networking.statistics.HostStatistics;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatusPacket implements SerializedPacket {

    private long freeRam;
    private int cpu;
    private Map<String, Map<Integer, Integer>> connections;

    @Override
    public void read(PacketSerializer packet) throws Exception {
        freeRam = packet.readLong();
        cpu = packet.readVarInt();

        int connectionSize = packet.readVarInt();
        connections = new HashMap<>(connectionSize, 1);
        for (int i = 0; i < connectionSize; i++) {
            String name = packet.readString();
            int instancesSize = packet.readVarInt();
            Map<Integer, Integer> instances = new HashMap<>(instancesSize, 1);
            for (int i2 = 0; i2 < instancesSize; i2++) {
                instances.put(packet.readVarInt(), packet.readVarInt());
            }
            connections.put(name, instances);
        }
    }

    @Override
    public void write(PacketSerializer packet) throws Exception {
        packet.writeLong(freeRam);
        packet.writeVarInt(cpu);

        packet.writeVarInt(connections.size());
        for (Map.Entry<String, Map<Integer, Integer>> service : connections.entrySet()) {
            packet.writeString(service.getKey());
            packet.writeVarInt(service.getValue().size());
            for (Map.Entry<Integer, Integer> instance : service.getValue().entrySet()) {
                packet.writeVarInt(instance.getKey());
                packet.writeVarInt(instance.getValue());
            }
        }
     }

     public HostStatistics toStatistics() {
        return new HostStatistics(cpu, freeRam, connections);
     }
}
