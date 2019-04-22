package io.github.splotycode.mosaik.networking.master;

import io.github.splotycode.mosaik.networking.master.packets.DestroyPacket;
import io.github.splotycode.mosaik.networking.master.packets.UpdateStatusPacket;
import io.github.splotycode.mosaik.networking.packet.handle.PacketTarget;
import io.github.splotycode.mosaik.networking.packet.handle.SelfAnnotationHandler;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MasterServerHandler extends SelfAnnotationHandler<SerializedPacket> {

    private MasterService service;

    @PacketTarget
    public void onStatusUpdate(UpdateStatusPacket packet) {

    }

    @PacketTarget
    public void onDestroy(DestroyPacket packet) {
        service.getServer().shutdown();
        if (service.getClient() != null) {
            service.getClient().shutdown();
        }
        service.createClient(packet.getBetterRoot());
    }

}
