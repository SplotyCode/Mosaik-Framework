package io.github.splotycode.mosaik.networking.component.template;

import io.github.splotycode.mosaik.networking.component.tcp.TCPClient;
import io.github.splotycode.mosaik.networking.component.udp.UDPClient;

public class ClientTemplate {

    public static ComponentTemplate<?, TCPClient<? extends TCPClient>> tcp() {
        return new ComponentTemplate<ComponentTemplate, TCPClient<? extends TCPClient>>() {
            @Override
            public TCPClient<? extends TCPClient> createComponent() {
                return TCPClient.create();
            }
        };
    }

    public static ComponentTemplate<?, UDPClient<? extends UDPClient>> udp() {
        return new ComponentTemplate<ComponentTemplate, UDPClient<? extends UDPClient>>() {
            @Override
            public UDPClient<? extends UDPClient> createComponent() {
                return UDPClient.create();
            }
        };
    }

}
