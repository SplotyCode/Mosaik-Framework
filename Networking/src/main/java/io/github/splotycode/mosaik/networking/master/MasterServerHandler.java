package io.github.splotycode.mosaik.networking.master;

import io.github.splotycode.mosaik.networking.host.Host;
import io.github.splotycode.mosaik.networking.master.host.RemoteMasterHost;
import io.github.splotycode.mosaik.networking.master.packets.DestroyPacket;
import io.github.splotycode.mosaik.networking.master.packets.DistributePacket;
import io.github.splotycode.mosaik.networking.packet.handle.PacketTarget;
import io.github.splotycode.mosaik.networking.packet.handle.SelfAnnotationHandler;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import io.github.splotycode.mosaik.networking.statistics.HostStatisticListener;
import io.github.splotycode.mosaik.networking.statistics.HostStatistics;
import io.github.splotycode.mosaik.networking.statistics.StatisticalHost;
import io.github.splotycode.mosaik.networking.util.MosaikAddress;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MasterServerHandler extends SelfAnnotationHandler<SerializedPacket> {

    protected final MasterService service;

    @PacketTarget
    public void onStatusUpdate(HostStatistics statistics, ChannelHandlerContext ctx) {
        Host rHost = service.getHostByCtx(ctx);
        if (rHost instanceof StatisticalHost) {
            StatisticalHost host = (StatisticalHost) rHost;
            host.update(statistics);
            service.cloudKit().getHandler().call(HostStatisticListener.class, listener -> listener.update(host));
        } else {
            throw new IllegalStateException("Got Update packet from a non statistical host");
        }
    }

    @PacketTarget
    public void onPacketDistribute(DistributePacket packet, ChannelHandlerContext ctx) {
        SerializedPacket distribute = packet.body();
        service.sendSelf(distribute);
        service.cloudKit().getHosts().forEach(host -> {
            if (host instanceof RemoteMasterHost) {
                RemoteMasterHost remoteHost = (RemoteMasterHost) host;
                if (remoteHost.getChannel() != ctx.channel()) {
                    remoteHost.getChannel().writeAndFlush(distribute);
                }
            }
        });
    }

    @PacketTarget
    public void onDestroy(DestroyPacket packet) {
        service.getServer().shutdown();
        if (service.getClient() != null) {
            service.getClient().shutdown();
        }
        service.createClient(new MosaikAddress(packet.getBetterRoot()));
    }

}
