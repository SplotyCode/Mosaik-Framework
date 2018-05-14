package me.david.davidlib.netty.client;

import lombok.Getter;
import lombok.Setter;
import me.david.davidlib.event.Cancelable;

public class ClientReconnectEvent extends ClientEvent implements Cancelable {

    @Getter @Setter private boolean canceled;
    @Getter @Setter private long sleepTime;

    public ClientReconnectEvent(INetClient client) {
        super(client);
    }
}
