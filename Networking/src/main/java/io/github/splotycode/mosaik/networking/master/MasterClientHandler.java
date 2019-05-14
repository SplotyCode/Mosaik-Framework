package io.github.splotycode.mosaik.networking.master;

import io.github.splotycode.mosaik.networking.packet.handle.SelfAnnotationHandler;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import io.github.splotycode.mosaik.networking.statistics.UpdateLocalStatisticsTask;
import io.netty.channel.ChannelHandlerContext;

public class MasterClientHandler extends SelfAnnotationHandler<SerializedPacket> {

    protected final MasterService service;
    private long updateID;

    public MasterClientHandler(MasterService service) {
        this.service = service;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        updateID = service.getTaskExecutor().runTask(new UpdateLocalStatisticsTask(service.getKit().getConfig(MasterService.DAEMON_STATS_DELAY), service.getKit(), ctx));
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        service.getTaskExecutor().stopTask(updateID);
        super.channelUnregistered(ctx);
    }
}
