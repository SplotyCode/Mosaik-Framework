package io.github.splotycode.mosaik.util;

import io.github.splotycode.mosaik.util.info.OperationSystem;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.function.Predicate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NetworkUtil {

    /**
     * Iterates over all addresses from all network interfaces.
     * It will return the address and its interface of the first Address that bypasses the filter
     * @param filter that filter that determines if a address is valid
     */
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

    /**
     * Gets the MAC Address
     */
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

    /**
     * Extracts the host string of a SocketAddress
     * @return the extracted host
     */
    public static String extractHost(SocketAddress address) {
        if (address instanceof InetSocketAddress) {
            return ((InetSocketAddress) address).getHostString();
        }
        return StringUtil.getLastSplit(address.toString(), "/");
    }

    /**
     * Checks if an address is local
     */
    public static boolean isLocalAddress(String address) {
        try {
            return isLocalAddress(InetAddress.getByName(address));
        } catch (UnknownHostException e) {
            return false;
        }
    }

    /**
     * Checks if an address is local
     */
    public static boolean isLocalAddress(InetAddress address) {
        if (address.isAnyLocalAddress() || address.isLoopbackAddress()) {
            return true;
        }
        try {
            return NetworkInterface.getByInetAddress(address) != null;
        } catch (SocketException e) {
            return false;
        }
    }


    /**
     * Gets the local ipv4 ip address
     */
    public static String localIp() {
        try {
            Pair<InetAddress, NetworkInterface> lanIp = getINetFromInterface(address -> address.getOne() instanceof Inet4Address);
            return lanIp == null ? null : lanIp.getOne().getHostAddress();
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Checks if we can connect to SocketAddress
     * @param host the socket address that should be checked
     * @param timeout after what time should we stop the check
     * @return true if it is online or false if the connection is timed or failed
     */
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

    /**
     * Checks if a host is online
     * @param preferINetReachable should we prefer {@link InetAddress#isReachable(int)}? This method <b>needs root</b>!
     * @param host the host to check
     * @param timeout after witch time (in milliseconds) should we assume that the host is offline?
     */
    public static boolean isOnline(boolean preferINetReachable, String host, int timeout) {
        if (preferINetReachable) {
            try {
                return InetAddress.getByName(host).isReachable(timeout);
            } catch (IOException e) {
                /* Just use OperationSystem to determine host  */
            }
        }
        return OperationSystem.isHostOnline(host, timeout);
    }

}
