package io.github.splotycode.mosaik.networking.packet;

import com.google.common.collect.HashBiMap;

public class PacketRegistry<P extends Packet> {

    private HashBiMap<Class<? extends P>, Integer> packetClasses = HashBiMap.create();

    public void register(Class<? extends P> clazz) {
        packetClasses.put(clazz, packetClasses.size());
    }

    public int getIdByPacket(Class<? extends P> clazz) {
        return packetClasses.get(clazz);
    }

    public int forceIdByPacket(Class<? extends P> clazz) {
        int id = getIdByPacket(clazz);
        if (id == -1) {
            throw new NullPointerException("Could not find packet " + clazz.getSimpleName() + " in registry");
        }
        return id;
    }

    public Class<? extends P> getPacketById(int id) {
        return packetClasses.inverse().get(id);
    }

    public Class<? extends P> forcePacketByID(int id) {
        Class<? extends P> packetClass = getPacketById(id);
        if (packetClass == null) {
            throw new IllegalStateException("Cloud not find Packet with id " + id);
        }
        return packetClass;
    }

}
