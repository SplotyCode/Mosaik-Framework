package io.github.splotycode.mosaik.networking.util;

import io.github.splotycode.mosaik.util.logger.Logger;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Objects;

public class MosaikAddress implements Comparable<MosaikAddress> {

    private static final Logger LOGGER = Logger.getInstance(MosaikAddress.class);

    private String rawAddress;
    private InetAddress address;

    public static MosaikAddress from(InetSocketAddress address) {
        return new MosaikAddress(address.getAddress());
    }

    public static MosaikAddress local() {
        try {
            return new MosaikAddress(InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            LOGGER.warn("Failed to get local InetAddress", e);
            return null;
        }
    }

    public MosaikAddress(String rawAddress) {
        this.rawAddress = rawAddress;
    }

    public MosaikAddress(InetAddress address) {
        this.address = address;
    }

    public void set(String rawAddress) {
        this.rawAddress = rawAddress;
        address = null;
    }

    public void set(InetAddress address) {
        this.address = address;
        rawAddress = null;
    }

    public InetAddress asAddress() {
        if (address == null) {
            try {
                address = InetAddress.getByName(rawAddress);
            } catch (UnknownHostException e) {
                LOGGER.warn("Failed to get InetAddress for " + rawAddress, e);
                address = null;
            }
        }
        return address;
    }

    public SocketAddress asSocketAddress(int port) {
        if (rawAddress == null) {
            return new InetSocketAddress(address, port);
        }
        return new InetSocketAddress(rawAddress, port);
    }

    public String asString() {
        if (rawAddress == null) {
            rawAddress = address.getHostAddress();
        }
        return rawAddress;
    }

    @Override
    public String toString() {
        return asString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MosaikAddress that = (MosaikAddress) o;
        if (rawAddress == null && that.rawAddress == null) {
            return address.equals(that.address);
        }
        return asString().equals(that.asString());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(asString());
    }

    @Override
    public int compareTo(MosaikAddress address) {
        return asString().compareTo(address.asString());
    }

}
