package me.david.davidlib.netty.packets;

import java.io.IOException;

public interface Packet<T> {

    void read(T packet) throws IOException;
    void write(T packet) throws IOException;

}
