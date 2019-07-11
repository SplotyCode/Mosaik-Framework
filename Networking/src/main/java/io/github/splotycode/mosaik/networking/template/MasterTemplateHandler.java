package io.github.splotycode.mosaik.networking.template;

import io.github.splotycode.mosaik.networking.packet.handle.PacketTarget;
import io.github.splotycode.mosaik.networking.template.packets.NoSyncPacket;
import io.github.splotycode.mosaik.networking.template.packets.RevertSyncPacket;
import io.github.splotycode.mosaik.networking.template.packets.StartSyncPacket;
import io.github.splotycode.mosaik.networking.template.packets.SyncPacket;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class MasterTemplateHandler {

    private TemplateService service;

    @PacketTarget
    public void onSync(SyncPacket packet) {
        SyncUtil.handleSync(packet, service);
    }

    @PacketTarget
    public void onSyncStart(StartSyncPacket packet, ChannelHandlerContext ctx) {
        HashMap<String, Long> local = service.newestMap();
        HashMap<String, Long> remote = packet.getNewest();

        /* Data that we will sync now */
        HashMap<String, Long> sync = new HashMap<>();
        /* Data that the client will need to sync */
        HashMap<String, Long> revert = new HashMap<>();

        for (Map.Entry<String, Long> entry : local.entrySet()) {
            long remoteUpdate = remote.getOrDefault(entry.getKey(), Long.MIN_VALUE);
            if (remoteUpdate < entry.getValue()) {
                sync.put(entry.getKey(), entry.getValue());
            } else if (remoteUpdate > entry.getValue()) {
                revert.put(entry.getKey(), entry.getValue());
            }
        }

        /* Has the servers that we don´t have? Sync them */
        for (Map.Entry<String, Long> entry : remote.entrySet()) {
            if (!local.containsKey(entry.getKey())) {
                revert.put(entry.getKey(), Long.MIN_VALUE);
            }
        }

        boolean send = false;
        if (!sync.isEmpty()) {
            send = true;
            ctx.writeAndFlush(SyncUtil.createSyncPacket(sync, service));
        }
        if (!revert.isEmpty()) {
            send = true;
            ctx.writeAndFlush(new RevertSyncPacket(revert));
        }

        /* No sync needed */
        if (!send) {
            ctx.writeAndFlush(new NoSyncPacket());
        }
    }

}
