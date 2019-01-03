package me.david.davidlib.netty;

import me.david.davidlib.netty.packets.Packet;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class PacketRegistry<T extends Packet> {

    private HashMap<Integer, Class<? extends T>> packets = new HashMap<>();

    private int id = -1;

    public PacketRegistry add(Class<? extends T> clazz) {
        id++;
        return add(id, clazz);
    }

    public PacketRegistry add(int id, Class<? extends T> clazz) {
        packets.put(id, clazz);
        return this;
    }

    public int getIdByPacket(T packet) {
        Optional<Map.Entry<Integer, Class<? extends T>>> optional =  packets.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), packet.getClass()))
                .findFirst();
        return optional.map(Map.Entry::getKey).orElse(-1);
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
