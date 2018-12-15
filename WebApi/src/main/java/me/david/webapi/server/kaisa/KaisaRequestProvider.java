package me.david.webapi.server.kaisa;

import me.david.davidlib.exception.MethodNotSupportedExcpetion;
import me.david.webapi.request.Method;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class KaisaRequestProvider {

    private SocketChannel connection;
    private String[] firstLine;

    public KaisaRequestProvider(SocketChannel connection) {
        this.connection = connection;
    }

    private String[] downloadFirstLine() {
        ByteBuffer buffer = ByteBuffer.allocate(64);
        throw new MethodNotSupportedExcpetion();
    }

    public String resolvePath() {
        if (firstLine == null) {
            firstLine = downloadFirstLine();
        }
        return firstLine[1];
    }

    public Method resolveMethod() {
        if (firstLine == null) {
            firstLine = downloadFirstLine();
        }
        return Method.create(firstLine[0]);
    }

    public byte[] resolveBody() {
        throw new MethodNotSupportedExcpetion();
    }

    public HashMap<String, String> resolveHeaders() {
        throw new MethodNotSupportedExcpetion();
    }

    public Map<String, ? extends Collection<String>> resolveGet() {
        throw new MethodNotSupportedExcpetion();
    }

    public Map<String, ? extends Collection<String>> resolvePost() {
        throw new MethodNotSupportedExcpetion();
    }

    public Map<String, String> resolveCookies() {
        throw new MethodNotSupportedExcpetion();
    }

}
