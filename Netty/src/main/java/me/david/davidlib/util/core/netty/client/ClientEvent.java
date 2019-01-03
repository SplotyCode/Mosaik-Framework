package me.david.davidlib.util.core.netty.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.david.davidlib.util.event.Event;

@AllArgsConstructor
public class ClientEvent extends Event {

    @Getter @Setter protected INetClient client;

}
