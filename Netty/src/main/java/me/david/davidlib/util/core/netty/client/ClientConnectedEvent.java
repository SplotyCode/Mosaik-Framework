package me.david.davidlib.util.core.netty.client;

public class ClientConnectedEvent extends ClientEvent {
    public ClientConnectedEvent(INetClient client) {
        super(client);
    }
}
