package io.github.splotycode.mosaik.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NetworkUtil {

    public static boolean isOnline(SocketAddress host, int timeout) {
        try {
            Socket socket = new Socket();
            socket.connect(host, timeout);
            socket.close();
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

}
