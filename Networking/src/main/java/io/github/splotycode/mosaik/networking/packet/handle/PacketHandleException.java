package io.github.splotycode.mosaik.networking.packet.handle;

public class PacketHandleException extends RuntimeException {

    public PacketHandleException() {
    }

    public PacketHandleException(String s) {
        super(s);
    }

    public PacketHandleException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public PacketHandleException(Throwable throwable) {
        super(throwable);
    }

    public PacketHandleException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
