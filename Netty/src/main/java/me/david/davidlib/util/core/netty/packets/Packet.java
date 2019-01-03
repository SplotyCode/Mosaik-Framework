package me.david.davidlib.util.core.netty.packets;

import java.io.IOException;

public interface Packet<R, W> {

    void read(R packet) throws IOException;
    void write(W packet) throws IOException;

}
