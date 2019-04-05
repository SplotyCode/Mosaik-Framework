package io.github.splotycode.mosaik.networking.component.template;

import io.github.splotycode.mosaik.networking.component.tcp.TCPClient;
import io.github.splotycode.mosaik.networking.component.udp.UDPClient;

public class ClientTemplate {

    private static class TCPClientTemplate extends ComponentTemplate<TCPClientTemplate, TCPClient> {

        @Override
        public TCPClient createComponent() {
            return TCPClient.create();
        }
    }

    private static class UDPClientTemplate extends ComponentTemplate<UDPClientTemplate, UDPClient> {

        @Override
        public UDPClient createComponent() {
            return UDPClient.create();
        }
    }

    public static ComponentTemplate<?, TCPClient> tcp() {
        return new TCPClientTemplate();
    }

    public static ComponentTemplate<?, UDPClient> udp() {
        return new UDPClientTemplate();
    }

}
