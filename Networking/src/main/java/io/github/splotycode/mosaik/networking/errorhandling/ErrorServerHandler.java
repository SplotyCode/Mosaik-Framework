package io.github.splotycode.mosaik.networking.errorhandling;

import io.github.splotycode.mosaik.networking.packet.handle.PacketTarget;
import io.github.splotycode.mosaik.networking.packet.handle.SelfAnnotationHandler;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import io.github.splotycode.mosaik.util.logger.Logger;
import io.netty.channel.ChannelHandler;
import lombok.AllArgsConstructor;

import java.util.function.Consumer;

@ChannelHandler.Sharable
@AllArgsConstructor
public class ErrorServerHandler extends SelfAnnotationHandler<SerializedPacket> {

    private static final Logger LOGGER = Logger.getInstance(ErrorServerHandler.class);

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
        service.getMasterService().sendPacket(packet.getRequester(), packet.toEnd());
    }

    @PacketTarget
    public void onBack(EndResolvePacket packet) {
        int callbackId = packet.getCallbackId();
        Consumer<ReportedError> callback = service.getCallBacks().remove(callbackId);
        if (callback == null) {
            LOGGER.warn("Could not find callback with id " + callbackId);
        } else {
            callback.accept(packet);
        }
    }

}
