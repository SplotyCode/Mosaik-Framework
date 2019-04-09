package io.github.splotycode.mosaik.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.function.Predicate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NetworkUtil {

    public static Pair<InetAddress, NetworkInterface> getINetFromInterface(Predicate<Pair<InetAddress, NetworkInterface>> filter) throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

        while (interfaces.hasMoreElements()) {
            NetworkInterface netInterface = interfaces.nextElement();
            Enumeration<InetAddress> addresses = netInterface.getInetAddresses();

            while (addresses.hasMoreElements()) {
                InetAddress ip = addresses.nextElement();
                if (ip.isSiteLocalAddress()) {
                    Pair<InetAddress, NetworkInterface> pair = new Pair<>(ip, netInterface);
                    if (filter.test(pair)) {
                        return pair;
                    }
                }
            }
        }
        return null;
    }

    public static String getMacAddress() {
        try {
            Pair<InetAddress, NetworkInterface> pair = getINetFromInterface(pair0 -> pair0.getOne() instanceof Inet4Address);
            if (pair == null) return null;
            byte[] mac = pair.getTwo().getHardwareAddress();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            return sb.toString();

        } catch (SocketException ex) {
            return null;
        }
    }


    public static String localIp() {
        try {
            Pair<InetAddress, NetworkInterface> lanIp = getINetFromInterface(address -> address.getOne() instanceof Inet4Address);
            return lanIp == null ? null : lanIp.getOne().getHostAddress();
        } catch (Exception ex) {
            return null;
        }
    }

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
