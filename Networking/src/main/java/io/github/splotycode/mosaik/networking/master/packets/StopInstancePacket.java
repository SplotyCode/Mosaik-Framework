package io.github.splotycode.mosaik.networking.master.packets;

import io.github.splotycode.mosaik.networking.packet.serialized.PacketSerializer;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StopInstancePacket implements SerializedPacket {

    private String serviceName;
    private int port;

    @Override
    public void read(PacketSerializer packet) throws Exception {
        serviceName = packet.readString();
        port = packet.readVarInt();
    }

    @Override
    public void write(PacketSerializer packet) throws Exception {
        packet.writeString(serviceName);
        packet.writeVarInt(port);
    }
}
