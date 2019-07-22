package io.github.splotycode.mosaik.networking.template;

import io.github.splotycode.mosaik.networking.component.NetworkComponent;
import io.github.splotycode.mosaik.networking.component.listener.BoundListener;
import io.github.splotycode.mosaik.networking.component.listener.UnBoundListener;
import io.github.splotycode.mosaik.networking.packet.handle.PacketTarget;
import io.github.splotycode.mosaik.networking.template.packets.*;
import io.github.splotycode.mosaik.util.io.FileUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TemplateHandler implements BoundListener, UnBoundListener {

    private TemplateService service;

    @PacketTarget
    public void onCreate(CreateTemplatePacket packet) {
        Template template = new Template(packet.getName(), service);
        service.getTemplates().put(packet.getName(), template);
        service.getIOQueue().execute(() -> template.getFile().mkdir());
    }

    @PacketTarget
    public void onDestroy(DestroyTemplatePacket packet) {
        Template template = service.getTemplates().remove(packet.getName());
        service.getIOQueue().execute(() -> FileUtil.delete(template.getFile()));
    }

    @PacketTarget
    public void onFileAdd(AddFilePacket packet) {
        Template template = service.getTemplates().get(packet.getTemplate());
        ResourceFile file = new ResourceFile(template, packet.getFile(), packet.getContent(), packet.getTime(), packet.getServer());
        template.addFileSynced(file);
    }

    @PacketTarget
    public void onSync(SyncPacket packet) {
        SyncUtil.handleSync(packet, service);
    }

    @PacketTarget
    public void onSyncFinish(NoSyncPacket packet) {
        service.setLoaded(true);
    }

    @PacketTarget
    public void onSyncRevert(StartSyncPacket packet, ChannelHandlerContext ctx) {
        ctx.writeAndFlush(SyncUtil.createSyncPacket(packet.getNewest(), service));
        onSyncFinish(null);
    }

    @Override
    public void bound(NetworkComponent component, ChannelFuture future) {
        future.channel().writeAndFlush(new StartSyncPacket(service.newestMap()));
    }

    @Override
    public void unBound(NetworkComponent component, ChannelFuture future) {
        service.setLoaded(false);
    }
}
