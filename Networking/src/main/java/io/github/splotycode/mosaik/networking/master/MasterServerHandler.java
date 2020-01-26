package io.github.splotycode.mosaik.networking.master;

import io.github.splotycode.mosaik.networking.host.Host;
import io.github.splotycode.mosaik.networking.master.host.MasterHost;
import io.github.splotycode.mosaik.networking.master.host.RemoteMasterHost;
import io.github.splotycode.mosaik.networking.master.packets.DestroyPacket;
import io.github.splotycode.mosaik.networking.master.packets.DirectContactPacket;
import io.github.splotycode.mosaik.networking.master.packets.DistributePacket;
import io.github.splotycode.mosaik.networking.master.packets.UpdateHostPacket;
import io.github.splotycode.mosaik.networking.packet.handle.PacketTarget;
import io.github.splotycode.mosaik.networking.packet.handle.SelfAnnotationHandler;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import io.github.splotycode.mosaik.networking.statistics.HostStatisticListener;
import io.github.splotycode.mosaik.networking.statistics.component.StatisticalHost;
import io.github.splotycode.mosaik.networking.statistics.network.UpdateSlavesTask;
import io.github.splotycode.mosaik.networking.util.MosaikAddress;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

@ChannelHandler.Sharable
public class MasterServerHandler extends SelfAnnotationHandler<SerializedPacket> implements MasterChangeListener {

    protected final MasterService service;
    private long taskID = -1;

    public MasterServerHandler(MasterService service) {
        this.service = service;
    }

    @PacketTarget
    public void onHostUpdate(UpdateHostPacket packet, ChannelHandlerContext ctx) throws Exception {
        try {
            packet.checkResolvable();
            Host rHost = service.getHostByCtx(ctx);
            if (rHost instanceof StatisticalHost) {
                StatisticalHost host = (StatisticalHost) rHost;
                host.statistics().read(packet.getUnresolvedBody());
                service.cloudKit().getHandler().call(HostStatisticListener.class, listener -> listener.update(host));
            }  else if (rHost == null) {
                throw new IllegalStateException("Could not find host by ctx");
            } else {
                throw new IllegalStateException("Tried to update non statistical host " + rHost);
            }
        } finally {
            packet.resolveDone();
        }
    }

    @PacketTarget
    public void onPacketDistribute(DistributePacket packet, ChannelHandlerContext ctx) {
        if (packet instanceof DirectContactPacket) {
            sendDirect((DirectContactPacket) packet);
            return;
        }
        SerializedPacket distribute = packet.body(service);
        service.sendSelf(distribute);
        service.cloudKit().getHosts().forEach(host -> {
            if (host instanceof RemoteMasterHost) {
                RemoteMasterHost remoteHost = (RemoteMasterHost) host;
                if (remoteHost.getChannel() != ctx.channel()) {
                    remoteHost.sendPacket(distribute);
                }
            }
        });
    }

    protected void sendDirect(DirectContactPacket packet) {
        ((MasterHost) service.cloudKit().getHostByAddress(packet.getAddress())).sendPacket(packet.body(service));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
    }

    @PacketTarget
    public void onDestroy(DestroyPacket packet) {
        service.getServer().shutdown();
        if (service.getClient() != null) {
            service.getClient().shutdown();
        }
        service.createClient(new MosaikAddress(packet.getBetterRoot()));
    }

    @Override
    public void onChange(boolean own, MosaikAddress current, MosaikAddress last) {
        if (own) {
            taskID = service.cloudKit().getLocalTaskExecutor().runTask(new UpdateSlavesTask(service));
        } else {
            service.cloudKit().getLocalTaskExecutor().stopTask(taskID);
        }
    }
}
