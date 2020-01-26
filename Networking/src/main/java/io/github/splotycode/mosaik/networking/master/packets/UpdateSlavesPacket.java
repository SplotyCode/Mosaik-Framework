package io.github.splotycode.mosaik.networking.master.packets;

import io.github.splotycode.mosaik.networking.packet.PostPacket;
import io.github.splotycode.mosaik.networking.packet.serialized.PacketSerializer;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import io.github.splotycode.mosaik.networking.statistics.HostStatistics;
import io.github.splotycode.mosaik.networking.util.MosaikAddress;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateSlavesPacket extends PostPacket<PacketSerializer, PacketSerializer> implements SerializedPacket {

    /* Only for write */
    private HashMap<MosaikAddress, HostStatistics> statistics;

    @Override
    public void write(PacketSerializer packet) throws Exception {
        packet.writeVarInt(statistics.size());
        for (Map.Entry<MosaikAddress, HostStatistics> host : statistics.entrySet()) {
            packet.writeString(host.getKey().asString());
            host.getValue().write(packet);
        }
    }
}
