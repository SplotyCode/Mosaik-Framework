package io.github.splotycode.mosaik.networking.config.packets;

import io.github.splotycode.mosaik.networking.packet.serialized.PacketSerializer;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KAUpdate implements SerializedPacket {

    private String key;
    private String newValue;

    @Override
    public void read(PacketSerializer packet) throws Exception {
        key = packet.readString();
        newValue = packet.readString();
    }

    @Override
    public void write(PacketSerializer packet) throws Exception {
        packet.writeString(key);
        packet.writeString(newValue);
    }
}
