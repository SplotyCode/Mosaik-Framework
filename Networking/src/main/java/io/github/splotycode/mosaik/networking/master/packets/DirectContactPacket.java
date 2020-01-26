package io.github.splotycode.mosaik.networking.master.packets;

import io.github.splotycode.mosaik.networking.master.MasterService;
import io.github.splotycode.mosaik.networking.packet.serialized.PacketSerializer;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import io.github.splotycode.mosaik.networking.util.MosaikAddress;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class DirectContactPacket extends DistributePacket {

    private MosaikAddress address;

    public DirectContactPacket(SerializedPacket body, MasterService service, MosaikAddress address) {
        super(body, service);
        this.address = address;
    }

    @Override
    public void write(PacketSerializer packet) throws Exception {
        packet.writeString(address.asString());
        super.write(packet);
    }

    @Override
    public void read(PacketSerializer packet) throws Exception {
        address = new MosaikAddress(packet.readString());
        super.read(packet);
    }
}
