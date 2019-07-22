package io.github.splotycode.mosaik.networking.config.packets;

import io.github.splotycode.mosaik.networking.packet.serialized.PacketSerializer;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConfigNoUpdate implements SerializedPacket {

    @Override
    public void read(PacketSerializer packet) {}

    @Override
    public void write(PacketSerializer packet) {}

}
