package io.github.splotycode.mosaik.networking.config.packets;

import io.github.splotycode.mosaik.networking.packet.serialized.PacketSerializer;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigRequestUpdate implements SerializedPacket {

    private String hash;

    @Override
    public void read(PacketSerializer packet) {
        hash = packet.readString();
    }

    @Override
    public void write(PacketSerializer packet) {
        packet.writeString(hash);
    }
}
