package io.github.splotycode.mosaik.networking.template.packets;

import io.github.splotycode.mosaik.networking.packet.serialized.PacketSerializer;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DestroyTemplatePacket implements SerializedPacket {

    private String name;

    @Override
    public void read(PacketSerializer packet) throws Exception {
        name = packet.readString();
    }

    @Override
    public void write(PacketSerializer packet) throws Exception {
        packet.writeString(name);
    }
}
