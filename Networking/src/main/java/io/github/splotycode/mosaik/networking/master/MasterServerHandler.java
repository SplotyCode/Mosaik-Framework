package io.github.splotycode.mosaik.networking.master;

import io.github.splotycode.mosaik.networking.master.packets.DestroyPacket;
import io.github.splotycode.mosaik.networking.packet.handle.PacketTarget;

public class MasterServerHandler {

    private MasterService service;

    @PacketTarget
    public void onDestroy(DestroyPacket packet) {
        service.getServer().shutdown();
        if (service.getClient() != null) {
            service.getClient().shutdown();
        }
        service.createClient(packet.getBetterRoot());
    }

}
