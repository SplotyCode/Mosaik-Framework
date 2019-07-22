package io.github.splotycode.mosaik.networking.template.packets;

import io.github.splotycode.mosaik.networking.packet.serialized.PacketSerializer;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StartSyncPacket implements SerializedPacket {

    private HashMap<String, Long> newest;

    @Override
    public void read(PacketSerializer packet) throws Exception {
        int size = packet.readVarInt();
        newest = new HashMap<>(size, 1);
        for (int i = 0; i < size; i++) {
            newest.put(packet.readString(), packet.readLong());
        }
    }

    @Override
    public void write(PacketSerializer packet) throws Exception {
        packet.writeVarInt(newest.size());
        for (Map.Entry<String, Long> entry : newest.entrySet()) {
            packet.writeString(entry.getKey());
            packet.writeLong(entry.getValue());
        }
    }
}
