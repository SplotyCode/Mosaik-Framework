package me.david.davidlib.netty.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;
import me.david.davidlib.netty.packets.BufPacket;
import me.david.davidlib.netty.PacketRegistry;

@AllArgsConstructor
public class BufPacketEncoder extends MessageToByteEncoder<BufPacket> {

    private PacketRegistry<BufPacket> packetRegistry;

    @Override
    protected void encode(ChannelHandlerContext ctx, BufPacket packet, ByteBuf output) throws Exception {
        try (ByteBufOutputStream os = new ByteBufOutputStream(output)) {
            int id = packetRegistry.getIdByPacket(packet);
            if(id == -1) {
                throw new NullPointerException("Could not find id to packet: " + packet.getClass().getSimpleName());
            }/* else {
                System.out.println("Encoder: " + id + " " + packet.getClass().getSimpleName());
            }*/
            os.writeInt(id);
            packet.write(os);
        }
    }

}
