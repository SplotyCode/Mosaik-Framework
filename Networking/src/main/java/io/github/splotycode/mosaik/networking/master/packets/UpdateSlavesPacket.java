package io.github.splotycode.mosaik.networking.master.packets;

import io.github.splotycode.mosaik.networking.packet.serialized.PacketSerializer;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import io.github.splotycode.mosaik.networking.statistics.HostStatistics;
import io.github.splotycode.mosaik.networking.util.MosaikAddress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
public class UpdateSlavesPacket implements SerializedPacket {

    private HashMap<MosaikAddress, HostStatistics> statistics;

    @Override
    public void read(PacketSerializer packet) throws Exception {
        int length = packet.readVarInt();
        statistics = new HashMap<>(length, 1);
        for (int i = 0; i < length; i++) {
            MosaikAddress address = new MosaikAddress(packet.readString());
            HostStatistics data = new HostStatistics();
            data.read(packet);
            statistics.put(address, data);
        }
    }

    @Override
    public void write(PacketSerializer packet) throws Exception {
        packet.writeVarInt(statistics.size());
        for (Map.Entry<MosaikAddress, HostStatistics> host : statistics.entrySet()) {
            packet.writeString(host.getKey().asString());
            host.getValue().write(packet);
        }
    }
}
