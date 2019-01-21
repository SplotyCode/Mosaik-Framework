package io.github.splotycode.mosaik.netty.client;

public class ClientConnectedEvent extends ClientEvent {
    public ClientConnectedEvent(INetClient client) {
        super(client);
    }
}
