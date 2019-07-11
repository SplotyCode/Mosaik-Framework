package io.github.splotycode.mosaik.networking.master.packets;

import io.github.splotycode.mosaik.networking.master.MasterService;
import io.github.splotycode.mosaik.networking.packet.serialized.PacketSerializer;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import io.github.splotycode.mosaik.util.ExceptionUtil;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DistributePacket implements SerializedPacket {

    private SerializedPacket body;
    private MasterService service;

    private PacketSerializer toRead;

    public DistributePacket(SerializedPacket body, MasterService service) {
        this.body = body;
        this.service = service;
    }

    public SerializedPacket body() {
        if (body == null) {
            if (toRead == null) {
                throw new IllegalStateException("Body has to be set in the constructor or packet has to be read");
            }
            try {
                body = service.getMasterRegistry().forcePacketByID(toRead.readVarInt()).newInstance();
                body.read(toRead);
            } catch (Exception ex) {
                ExceptionUtil.throwRuntime(ex, "Failed to post read packet");
            }
        }
        return body;
    }

    @Override
    public void read(PacketSerializer packet) throws Exception {
        toRead = packet;
    }

    @Override
    public void write(PacketSerializer packet) throws Exception {
        packet.writeVarInt(service.getMasterRegistry().forceIdByPacket(body.getClass()));
        body.write(packet);
    }
}
