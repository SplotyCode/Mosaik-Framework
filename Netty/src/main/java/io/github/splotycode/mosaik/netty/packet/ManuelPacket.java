package io.github.splotycode.mosaik.netty.packet;

public interface ManuelPacket<R, W> extends Packet {

    void read(R packet) throws Exception;
    void write(W packet) throws Exception;

}
