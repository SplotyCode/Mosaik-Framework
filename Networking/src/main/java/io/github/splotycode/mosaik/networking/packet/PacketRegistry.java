package io.github.splotycode.mosaik.networking.packet;

import com.google.common.collect.HashBiMap;
import io.github.splotycode.mosaik.util.reflection.GenericGuesser;
import io.github.splotycode.mosaik.util.reflection.classregister.RawClassRegister;

public class PacketRegistry<P extends Packet> implements RawClassRegister<Class<? extends P>> {

    private HashBiMap<Class<? extends P>, Integer> packetClasses = HashBiMap.create();
    private Class<? extends P> packetClass;

    public PacketRegistry() {
        try {
            packetClass = (Class<? extends P>) GenericGuesser.find(getClass(), PacketRegistry.class, "P");
            assert packetClass != null;
        } catch (Throwable e) {
            throw new IllegalStateException("GenericGuesser could not find type please specify the type", e);
        }
    }

    public PacketRegistry(Class<? extends P> packetClass) {
        this.packetClass = packetClass;
    }

    @Override
    public void register(Class<? extends P> clazz) {
        packetClasses.put(clazz, packetClasses.size());
    }

    @Override
    public void unRegister(Class<? extends P> clazz) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<? extends P> getObjectClass() {
        return packetClass;
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
