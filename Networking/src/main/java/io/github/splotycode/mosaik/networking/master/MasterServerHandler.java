package io.github.splotycode.mosaik.networking.master;

import io.github.splotycode.mosaik.networking.host.Host;
import io.github.splotycode.mosaik.networking.master.packets.DestroyPacket;
import io.github.splotycode.mosaik.networking.master.packets.UpdateStatusPacket;
import io.github.splotycode.mosaik.networking.packet.handle.PacketTarget;
import io.github.splotycode.mosaik.networking.packet.handle.SelfAnnotationHandler;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import io.github.splotycode.mosaik.networking.statistics.StatisticalHost;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MasterServerHandler extends SelfAnnotationHandler<SerializedPacket> {

    protected final MasterService service;

    @PacketTarget
    public void onStatusUpdate(UpdateStatusPacket packet, ChannelHandlerContext ctx) {
        Host host = service.getHostByCtx(ctx);
        if (host instanceof StatisticalHost) {
            ((StatisticalHost) host).update(packet.toStatistics());
        } else {
            throw new IllegalStateException("Got Update packet from a non statistical host");
        }
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
