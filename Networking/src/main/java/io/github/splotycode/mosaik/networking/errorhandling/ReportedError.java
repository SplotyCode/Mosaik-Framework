package io.github.splotycode.mosaik.networking.errorhandling;

import io.github.splotycode.mosaik.networking.packet.serialized.PacketSerializer;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import io.github.splotycode.mosaik.networking.util.MosaikAddress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReportedError implements SerializedPacket, Cloneable {

    protected HashMap<MosaikAddress, HashMap<String, String>> servers;

    @Override
    public void read(PacketSerializer packet) throws Exception {
        int length  = packet.readVarInt();
        servers = new HashMap<>(length, 1);
        for (int i = 0; i < length; i++) {
            MosaikAddress address = new MosaikAddress(packet.readString());

            int logs = packet.readVarInt();
            HashMap<String, String> log = new HashMap<>(logs, 1);
            for (int j = 0; j < logs; j++) {
                log.put(packet.readString(), packet.readString());
            }

            servers.put(address, log);
        }
    }

    @Override
    public void write(PacketSerializer packet) throws Exception {
        packet.writeVarInt(servers.size());
        for (Map.Entry<MosaikAddress, HashMap<String, String>> host : servers.entrySet()) {
            packet.writeString(host.getKey().asString());

            packet.writeVarInt(host.getValue().size());
            for (Map.Entry<String, String> log : host.getValue().entrySet()) {
                packet.writeString(log.getKey());
                packet.writeString(log.getValue());
            }
        }
    }

    @Override
    public ReportedError clone() {
        return new ReportedError(servers);
    }
}
