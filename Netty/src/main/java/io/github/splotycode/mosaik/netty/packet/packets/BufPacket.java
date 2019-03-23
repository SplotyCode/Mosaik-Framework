package io.github.splotycode.mosaik.netty.packet.packets;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

public interface BufPacket extends Packet<ByteBufInputStream, ByteBufOutputStream>  {

}
