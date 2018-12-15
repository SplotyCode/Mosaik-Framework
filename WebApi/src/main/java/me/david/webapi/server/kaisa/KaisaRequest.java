package me.david.webapi.server.kaisa;

import lombok.Getter;
import lombok.Setter;
import me.david.davidlib.exception.MethodNotSupportedExcpetion;
import me.david.davidlib.helper.AlmostBoolean;
import me.david.davidlib.utils.EnumUtil;
import me.david.webapi.request.Method;
import me.david.webapi.request.Request;
import me.david.webapi.request.RequestHeaders;
import me.david.webapi.response.Response;
import me.david.webapi.server.netty.NettyUtils;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class KaisaRequest implements Request {

    private KaisaRequestProvider provider;

    private String path = null, ip = null;
    private Method method = null;
    private SocketChannel connection;
    private AlmostBoolean isPost = AlmostBoolean.MAYBE, isGet = AlmostBoolean.MAYBE;
    private Response response = new Response(null);
    
    private HashMap<String, String> headers;
    @Getter @Setter private Map<String, ? extends Collection<String>> get;
    @Getter @Setter private Map<String, ? extends Collection<String>> post;
    private Map<String, String> cookies;

    private byte[] body = null;

    public KaisaRequest(SocketChannel connection) {
        this.connection = connection;
    }

    @Override
    public String getPath() {
        if (path == null) {
            path = provider.resolvePath();
        }
        return path;
    }

    @Override
    public Method getMethod() {
        if (method == null) {
            method = provider.resolveMethod();
        }
        return method;
    }

    @Override
    public Response getResponse() {
        return response;
    }

    @Override
    public String getIpAddress() {
        if (ip == null) {
            try {
                ip = NettyUtils.transformIpAddress(connection.getRemoteAddress().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ip;
    }

    @Override
    public byte[] getBody() {
        if (body == null) {
            body = provider.resolveBody();
        }
        return body;
    }

    //TODO
    @Override
    public boolean isKeepAlive() {
        throw new MethodNotSupportedExcpetion();
    }

    @Override
    public String getHeader(String name) {
        if (headers == null) {
            headers = provider.resolveHeaders();
        }
        return headers.get(name);
    }

    @Override
    public String getHeader(RequestHeaders header) {
        if (headers == null) {
            headers = provider.resolveHeaders();
        }
        return headers.get(EnumUtil.toDisplayName(header));
    }

    @Override
    public HashMap<String, String> getHeaders() {
        if (headers == null) {
            headers = provider.resolveHeaders();
        }
        return headers;
    }

    @Override
    public Collection<String> getGetParameter(String name) {
        if (get == null) {
            get = provider.resolveGet();
        }
        return get.get(name);
    }

    @Override
    public Collection<String> getPostParameter(String name) {
        if (post == null) {
            post = provider.resolveGet();
        }
        return post.get(name);
    }

    @Override
    public String getFirstGetParameter(String name) {
        return getGetParameter(name).iterator().next();
    }

    @Override
    public String getFirstPostParameter(String name) {
        return getGetParameter(name).iterator().next();
    }

    @Override
    public boolean isGet() {
        if (isGet.isMaybe()) {
            if (method == null) {
                method = provider.resolveMethod();
            }
            isGet = AlmostBoolean.fromBoolean(method.isStandard() && method.getStandardMethod() == Method.StandardMethod.GET);
        }
        return isGet.decide(true);
    }

    @Override
    public boolean isPost(){
        if (isPost.isMaybe()) {
            if (method == null) {
                method = provider.resolveMethod();
            }
            isPost = AlmostBoolean.fromBoolean(method.isStandard() && method.getStandardMethod() == Method.StandardMethod.POST);
        }
        return isPost.decide(true);
    }

    @Override
    public Map<String, String> getCookies() {
        if (cookies == null) {
            cookies = provider.resolveCookies();
        }
        return cookies;
    }

}
