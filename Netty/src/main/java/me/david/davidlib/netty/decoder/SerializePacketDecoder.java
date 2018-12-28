package me.david.davidlib.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;
import me.david.davidlib.netty.PacketRegistry;
import me.david.davidlib.netty.PacketSerializer;
import me.david.davidlib.netty.packets.SerializePacket;

import java.util.List;

@AllArgsConstructor
public class SerializePacketDecoder extends ByteToMessageDecoder {

    private PacketRegistry<SerializePacket> packetRegistry;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf bytebuf, List<Object> output) throws Exception {
        PacketSerializer ps = new PacketSerializer(bytebuf);
        int id = ps.readVarInt();
        SerializePacket packet = packetRegistry.createPacket(id);
        if (packet == null) {
            throw new NullPointerException("Cloud not find that Packet");
        }/* else {
            System.out.println("Docoder: Id: " + id + " " + packet.getClass().getSimpleName());
        }*/
        packet.read(ps);
        output.add(packet);
    }

}
