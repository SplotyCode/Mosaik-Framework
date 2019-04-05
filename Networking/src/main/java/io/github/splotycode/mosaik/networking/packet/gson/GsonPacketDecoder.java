package io.github.splotycode.mosaik.networking.packet.gson;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.splotycode.mosaik.networking.packet.PacketRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;

import java.nio.charset.Charset;
import java.util.List;

@AllArgsConstructor
public class GsonPacketDecoder extends ByteToMessageDecoder {

    private PacketRegistry<GsonPacket> registry;

    private static final Gson GSON = new Gson();
    private static final JsonParser JSON_PARSER = new JsonParser();
    private static final Charset CHARSET = Charset.forName("UTF-8");

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        String text = new String(bytes, CHARSET);

        JsonObject o = JSON_PARSER.parse(text).getAsJsonObject();

        Class<? extends GsonPacket> clazz = registry.getPacketById(o.get("id").getAsInt());
        if(clazz != null) {
            o.remove("id");

            GsonPacket packet = GSON.fromJson(o, clazz);
            list.add(packet);
        }
    }

}
