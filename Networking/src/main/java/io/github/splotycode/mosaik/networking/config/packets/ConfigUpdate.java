package io.github.splotycode.mosaik.networking.config.packets;

import io.github.splotycode.mosaik.networking.packet.serialized.PacketSerializer;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class ConfigUpdate implements SerializedPacket {

    private String config;

    @Override
    public void read(PacketSerializer packet) {
        config = packet.readString();
    }

    @Override
    public void write(PacketSerializer packet) {
        packet.writeString(config);
    }
}
