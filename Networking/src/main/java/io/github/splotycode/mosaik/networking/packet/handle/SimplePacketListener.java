package io.github.splotycode.mosaik.networking.packet.handle;

import io.github.splotycode.mosaik.networking.packet.Packet;
import io.github.splotycode.mosaik.util.reflection.GenericGuesser;

public abstract class SimplePacketListener<P extends Packet> implements PacketListener {

    private Class clazz;

    public SimplePacketListener() {
        clazz = GenericGuesser.find(this, SimplePacketListener.class, "P");
    }

    @Override
    public void onPacket(Packet packet) {
        if (clazz.isInstance(packet)) {
            @SuppressWarnings("unchecked")
            P packet0 = (P) packet;
            onPacket0(packet0);
        }
    }

    public abstract void onPacket0(P packet);

}
