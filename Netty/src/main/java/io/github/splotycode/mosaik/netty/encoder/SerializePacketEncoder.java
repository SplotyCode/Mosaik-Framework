package io.github.splotycode.mosaik.netty.encoder;

import io.github.splotycode.mosaik.netty.PacketSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;
import io.github.splotycode.mosaik.netty.PacketRegistry;
import io.github.splotycode.mosaik.netty.packets.SerializePacket;

@AllArgsConstructor
public class SerializePacketEncoder extends MessageToByteEncoder<SerializePacket> {

    private PacketRegistry<SerializePacket> packetRegistry;

    @Override
    protected void encode(ChannelHandlerContext ctx, SerializePacket packet, ByteBuf output) throws Exception {
        int id = packetRegistry.getIdByPacket(packet);
        if(id == -1) {
            throw new NullPointerException("Could not find id to packet: " + packet.getClass().getSimpleName());
        }/* else {
            System.out.println("Encoder: Id: " + id + " " + packet.getClass().getSimpleName());
        }*/
        PacketSerializer ps = new PacketSerializer(output);
        ps.writeVarInt(id);
        packet.write(ps);
    }

}
