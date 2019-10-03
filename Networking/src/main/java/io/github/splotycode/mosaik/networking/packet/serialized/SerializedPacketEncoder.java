package io.github.splotycode.mosaik.networking.packet.serialized;

import io.github.splotycode.mosaik.networking.packet.PacketRegistry;
import io.github.splotycode.mosaik.util.logger.Logger;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SerializedPacketEncoder extends MessageToByteEncoder<SerializedPacket> {

	private static final Logger LOGGER = Logger.getInstance(SerializedPacketEncoder.class);

	private PacketRegistry<SerializedPacket> registry;

	@Override
	protected void encode(ChannelHandlerContext ctx, SerializedPacket packet, ByteBuf output) throws Exception {
		LOGGER.debug("Sending Packet: " + packet);
		PacketSerializer ps = new PacketSerializer(output);
		ps.writeVarInt(registry.forceIdByPacket(packet.getClass()));
		packet.write(ps);
	}

}
