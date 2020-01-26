package io.github.splotycode.mosaik.networking.master.packets;

import io.github.splotycode.mosaik.networking.packet.PostPacket;
import io.github.splotycode.mosaik.networking.packet.serialized.PacketSerializer;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateHostPacket extends PostPacket<PacketSerializer, PacketSerializer> implements SerializedPacket {

    /* Only for write */
    protected SerializedPacket hostStatistics;

    @Override
    public void write(PacketSerializer packet) throws Exception {
        hostStatistics.write(packet);
    }
}
