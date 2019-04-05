package io.github.splotycode.mosaik.networking.packet.buf;

import io.github.splotycode.mosaik.networking.packet.ManuelPacket;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

public interface BufPacket extends ManuelPacket<ByteBufInputStream, ByteBufOutputStream> {

}
