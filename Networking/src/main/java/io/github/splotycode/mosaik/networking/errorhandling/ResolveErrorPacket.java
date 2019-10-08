package io.github.splotycode.mosaik.networking.errorhandling;

import io.github.splotycode.mosaik.networking.packet.serialized.PacketSerializer;
import io.github.splotycode.mosaik.networking.util.MosaikAddress;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class ResolveErrorPacket extends ReportedError {

    private int callbackId;
    private MosaikAddress requester;
    private String currentID;

    public ResolveErrorPacket(HashMap<MosaikAddress, HashMap<String, String>> servers,
                              int callbackId, MosaikAddress requester,
                              String currentID) {
        super(servers);
        this.callbackId = callbackId;
        this.requester = requester;
        this.currentID = currentID;
    }

    @Override
    public void read(PacketSerializer packet) throws Exception {
        super.read(packet);
        callbackId = packet.readVarInt();
        requester = new MosaikAddress(packet.readString());
        currentID = packet.readString();
    }

    @Override
    public void write(PacketSerializer packet) throws Exception {
        super.write(packet);
        packet.writeVarInt(callbackId);
        packet.writeString(requester.asString());
        packet.writeString(currentID);
    }

    @Override
    public ResolveErrorPacket clone() {
        return new ResolveErrorPacket(servers, callbackId, requester, currentID);
    }
}
