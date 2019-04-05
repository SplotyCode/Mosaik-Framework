package io.github.splotycode.mosaik.networking.packet.gson;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.splotycode.mosaik.networking.packet.PacketRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GsonPacketEncoder extends MessageToByteEncoder<GsonPacket> {

    private static final Gson GSON = new Gson();

    private PacketRegistry<GsonPacket> registry;

    @Override
    protected void encode(ChannelHandlerContext ctx, GsonPacket packet, ByteBuf byteBuf) throws Exception {
        int id = registry.getIdByPacket(packet.getClass());

        String json = GSON.toJson(packet);

        JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        obj.addProperty("id", id);

        byteBuf.writeBytes(GSON.toJson(obj).getBytes());
    }

}
