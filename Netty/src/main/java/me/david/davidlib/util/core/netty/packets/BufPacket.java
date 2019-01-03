package me.david.davidlib.util.core.netty.packets;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

public interface BufPacket extends Packet<ByteBufInputStream, ByteBufOutputStream>  {

}
