package io.github.splotycode.mosaik.netty.client;

import lombok.Getter;
import lombok.Setter;
import io.github.splotycode.mosaik.util.event.Cancelable;

public class ClientReconnectEvent extends ClientEvent implements Cancelable {

    @Getter @Setter private boolean canceled;
    @Getter @Setter private long sleepTime;

    public ClientReconnectEvent(INetClient client) {
        super(client);
    }
}
