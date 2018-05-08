package me.david.davidlib.netty;

import me.david.davidlib.netty.packets.Packet;

import java.util.HashMap;
import java.util.Objects;

public class PacketRegistry<T extends Packet> {

    private HashMap<Integer, Class<? extends T>> packets = new HashMap<>();

    public PacketRegistry add(int id, Class<? extends T> clazz) {
        packets.put(id, clazz);
        return this;
    }

    public int getIdByPacket(T packet) {
        return packets.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), packet.getClass()))
                .findFirst().get().getKey();
    }

    public Class<? extends T> getPacketById(int id) {
        return packets.get(id);
    }

    public T createPacket(int id) throws ReflectiveOperationException {
        Class<? extends T> clazz = this.getPacketById(id);
        if(clazz != null)
            return clazz.newInstance();
        return null;
    }

    public boolean exists(int id) {
        return packets.containsKey(id);
    }

}
