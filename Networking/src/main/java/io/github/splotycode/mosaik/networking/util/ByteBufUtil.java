package io.github.splotycode.mosaik.networking.util;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ByteBufUtil {

    public static byte[] toArray(ByteBuf buf) {
        if (buf.hasArray()) {
            return buf.array();
        }
        byte[] bytes = new byte[buf.readableBytes()];
        buf.getBytes(buf.readerIndex(), bytes);
        return bytes;
    }

    public static byte[] readBytes(ByteBuf buf, int length) {
        byte[] bytes = new byte[length];
        buf.readBytes(bytes);
        return bytes;
    }

    public static byte[] drain(ByteBuf buf) {
        return readBytes(buf, buf.readableBytes());
    }

}
