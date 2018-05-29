package me.david.davidlib.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;
import me.david.davidlib.netty.PacketRegistry;
import me.david.davidlib.netty.PacketSerializer;
import me.david.davidlib.netty.packets.BufPacket;
import me.david.davidlib.netty.packets.SerializePacket;

import java.util.List;

@AllArgsConstructor
public class BufPacketDecoder extends ByteToMessageDecoder {

    private PacketRegistry<BufPacket> packetRegistry;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf bytebuf, List<Object> output) throws Exception {
        try (ByteBufInputStream is = new ByteBufInputStream(bytebuf)) {
            int id = is.readInt();
            BufPacket packet = packetRegistry.createPacket(id);
            if(packet == null) {
                throw new NullPointerException("Cloud not find that Packet");
            } else System.out.println("Decoder: " + id + " " + packet.getClass().getSimpleName());
            packet.read(is);
            output.add(packet);
        }
    }

}
