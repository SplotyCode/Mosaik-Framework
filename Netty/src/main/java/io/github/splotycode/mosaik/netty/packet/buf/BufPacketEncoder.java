package io.github.splotycode.mosaik.netty.packet.buf;

import io.github.splotycode.mosaik.netty.packet.PacketRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BufPacketEncoder extends MessageToByteEncoder<BufPacket> {

    private PacketRegistry<BufPacket> registry;

    @Override
    protected void encode(ChannelHandlerContext ctx, BufPacket packet, ByteBuf byteBuf) throws Exception {
        try (ByteBufOutputStream os = new ByteBufOutputStream(byteBuf)) {
            os.writeInt(registry.getIdByPacket(packet.getClass()));
            packet.write(os);
        }
    }

}
