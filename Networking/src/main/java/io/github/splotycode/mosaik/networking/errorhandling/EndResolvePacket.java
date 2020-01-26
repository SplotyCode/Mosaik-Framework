package io.github.splotycode.mosaik.networking.errorhandling;

import io.github.splotycode.mosaik.networking.packet.serialized.PacketSerializer;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import io.github.splotycode.mosaik.networking.util.MosaikAddress;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class EndResolvePacket extends ReportedError implements SerializedPacket {

    private int callbackId;

    public EndResolvePacket(HashMap<MosaikAddress, HashMap<String, String>> servers, int callbackId) {
        super(servers);
        this.callbackId = callbackId;
    }

    @Override
    public void write(PacketSerializer packet) throws Exception {
        super.write(packet);
        packet.writeVarInt(callbackId);
    }

    @Override
    public void read(PacketSerializer packet) throws Exception {
        super.read(packet);
        callbackId = packet.readVarInt();
    }
}
