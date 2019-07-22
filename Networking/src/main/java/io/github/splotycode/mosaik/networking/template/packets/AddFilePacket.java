package io.github.splotycode.mosaik.networking.template.packets;

import io.github.splotycode.mosaik.networking.packet.serialized.PacketSerializer;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddFilePacket implements SerializedPacket {

    private String template;
    private String file;
    private long time;
    private String server;
    private byte[] content;

    @Override
    public void read(PacketSerializer packet) throws Exception {
        template = packet.readString();
        file = packet.readString();
        time = packet.readLong();
        server = packet.readString();
        content = packet.drain();
    }

    @Override
    public void write(PacketSerializer packet) throws Exception {
        packet.writeString(template);
        packet.writeString(file);
        packet.writeLong(time);
        packet.writeString(server);
        packet.writeBytes(content);
    }
}
