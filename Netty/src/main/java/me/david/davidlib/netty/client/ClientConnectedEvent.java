package me.david.davidlib.netty.client;

public class ClientConnectedEvent extends ClientEvent {
    public ClientConnectedEvent(INetClient client) {
        super(client);
    }
}
