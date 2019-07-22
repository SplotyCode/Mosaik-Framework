package io.github.splotycode.mosaik.networking.packet.handle;

import io.github.splotycode.mosaik.networking.packet.Packet;
import io.github.splotycode.mosaik.util.listener.Listener;

public interface PacketListener extends Listener {

    void onPacket(Packet packet);

}
