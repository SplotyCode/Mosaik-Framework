package io.github.splotycode.mosaik.networking.packet.serialized;

import io.github.splotycode.mosaik.networking.packet.PacketRegistry;
import io.github.splotycode.mosaik.util.logger.Logger;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class SerializedPacketDecoder extends ByteToMessageDecoder {

	private static final Logger LOGGER = Logger.getInstance(SerializedPacketDecoder.class);

	private PacketRegistry<SerializedPacket> registry;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf bytebuf, List<Object> output) throws Exception {
		PacketSerializer ps = new PacketSerializer(bytebuf);
		SerializedPacket p = registry.forcePacketByID(ps.readVarInt()).newInstance();
		p.read(ps);
		LOGGER.debug("Received packet: " + p);
		output.add(p);
	}

}
