package io.github.splotycode.mosaik.networking.packet.buf;

import io.github.splotycode.mosaik.networking.packet.PacketRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class BufPacketDecoder extends ByteToMessageDecoder {

    private PacketRegistry<BufPacket> registry;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        try (ByteBufInputStream is = new ByteBufInputStream(byteBuf)) {
            int id = is.readInt();
            Class<? extends BufPacket> packetClass = registry.getPacketById(id);
            if (packetClass == null) {
                throw new NullPointerException("Coud not find that Packet");
            }
            list.add(packetClass.newInstance());
        }
    }
}
