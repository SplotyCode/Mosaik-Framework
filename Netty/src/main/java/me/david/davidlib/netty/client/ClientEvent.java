package me.david.davidlib.netty.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.david.davidlib.util.event.Event;

@AllArgsConstructor
public class ClientEvent extends Event {

    @Getter @Setter protected INetClient client;

}
