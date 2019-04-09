package io.github.splotycode.mosaik.networking.packet.handle;

import io.github.splotycode.mosaik.networking.packet.Packet;
import io.github.splotycode.mosaik.util.listener.Listener;

public interface PacketListener<P extends Packet> extends Listener {

    void onPacket(P packet);

}
