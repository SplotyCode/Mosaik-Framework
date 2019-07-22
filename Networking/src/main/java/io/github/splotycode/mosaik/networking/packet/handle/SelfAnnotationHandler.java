package io.github.splotycode.mosaik.networking.packet.handle;

import io.github.splotycode.mosaik.networking.packet.Packet;

public class SelfAnnotationHandler<P extends Packet> extends AnnotationContentHandler<P> {

    public SelfAnnotationHandler() {
        register(this);
    }

}
