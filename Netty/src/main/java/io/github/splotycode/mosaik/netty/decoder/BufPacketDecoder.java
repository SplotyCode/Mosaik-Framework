package io.github.splotycode.mosaik.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;
import io.github.splotycode.mosaik.netty.PacketRegistry;
import io.github.splotycode.mosaik.netty.packets.BufPacket;

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
            }/* else {
                System.out.println("Decoder: " + id + " " + packet.getClass().getSimpleName());
            }*/
            packet.read(is);
            output.add(packet);
        }
    }

}
