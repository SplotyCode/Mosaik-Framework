package io.github.splotycode.mosaik.networking.errorhandling;

import io.github.splotycode.mosaik.networking.packet.handle.PacketTarget;
import io.github.splotycode.mosaik.networking.packet.handle.SelfAnnotationHandler;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import io.netty.channel.ChannelHandler;
import lombok.AllArgsConstructor;

@ChannelHandler.Sharable
@AllArgsConstructor
public class ErrorServerHandler extends SelfAnnotationHandler<SerializedPacket> {

    private ErrorService service;

    @PacketTarget
    public void onTrace(ResolveErrorPacket packet) {
        ErrorEntry entry = service.getLocalErrorEntries().get(packet.getCurrentID());
        if (entry != null) {
            packet.servers.put(entry.getId().getHost(), entry.getLog());
            ErrorEntryID prev = entry.getPrevious();
            if (prev != null) {
                packet.setCurrentID(prev.getId());
                service.sendNext(prev.getHost(), packet);
                return;
            }
        }
        /* send information back to the original requester */
        //TODO
    }

    @PacketTarget
    public void onBack() {
        //TODO call callback based on callbackid
    }

}
