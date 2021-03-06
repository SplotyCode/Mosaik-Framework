package io.github.splotycode.mosaik.networking.packet.serialized;

import io.github.splotycode.mosaik.networking.packet.PacketRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SerializedPacketEncoder extends MessageToByteEncoder<SerializedPacket> {

	private PacketRegistry<SerializedPacket> registry;

	@Override
	protected void encode(ChannelHandlerContext ctx, SerializedPacket packet, ByteBuf output) throws Exception {
		PacketSerializer ps = new PacketSerializer(output);
		ps.writeVarInt(registry.forceIdByPacket(packet.getClass()));
		packet.write(ps);
	}

}
