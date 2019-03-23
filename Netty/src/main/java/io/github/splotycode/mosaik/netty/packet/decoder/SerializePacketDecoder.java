package io.github.splotycode.mosaik.netty.packet.decoder;

import io.github.splotycode.mosaik.netty.packet.PacketSerializer;
import io.github.splotycode.mosaik.netty.packet.packets.SerializePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;
import io.github.splotycode.mosaik.netty.packet.PacketRegistry;

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
