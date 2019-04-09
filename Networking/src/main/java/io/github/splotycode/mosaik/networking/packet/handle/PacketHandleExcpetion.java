package io.github.splotycode.mosaik.networking.packet.handle;

public class PacketHandleExcpetion extends RuntimeException {

    public PacketHandleExcpetion() {
    }

    public PacketHandleExcpetion(String s) {
        super(s);
    }

    public PacketHandleExcpetion(String s, Throwable throwable) {
        super(s, throwable);
    }

    public PacketHandleExcpetion(Throwable throwable) {
        super(throwable);
    }

    public PacketHandleExcpetion(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
