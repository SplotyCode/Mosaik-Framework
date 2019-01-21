package io.github.splotycode.mosaik.netty.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import io.github.splotycode.mosaik.util.event.Event;

@AllArgsConstructor
public class ClientEvent extends Event {

    @Getter @Setter protected INetClient client;

}
