package io.github.splotycode.mosaik.networking.master.packets;

import io.github.splotycode.mosaik.networking.packet.serialized.PacketSerializer;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DestroyPacket implements SerializedPacket {

    private String betterRoot;

    @Override
    public void read(PacketSerializer packet) {
        betterRoot = packet.readString();
    }

    @Override
    public void write(PacketSerializer packet) {
        packet.writeString(betterRoot);
    }

}
