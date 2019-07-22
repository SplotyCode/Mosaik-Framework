package io.github.splotycode.mosaik.webapi.server.netty;

import io.github.splotycode.mosaik.util.datafactory.DataKey;
import io.github.splotycode.mosaik.webapi.server.WebServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.github.splotycode.mosaik.util.AlmostBoolean;
import io.github.splotycode.mosaik.webapi.request.AbstractRequest;
import io.github.splotycode.mosaik.webapi.request.Method;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NettyRequest extends AbstractRequest {

    public static final DataKey<ChannelHandlerContext> CTX_KEY = new DataKey<>("netty.ctx");

    private FullHttpRequest request;
    private ChannelHandlerContext ctx;

    private QueryStringDecoder uri = null;
    private String prettyPath = null;

    private Method method = null;
    private byte[] content = null;
    private String address = null;
    private AlmostBoolean keepAlive = AlmostBoolean.MAYBE;
    private Map<String, String> cookies;
    private HashMap<String, String> headers = null;
    private Map<String, ? extends Collection<String>> post = null;

    public NettyRequest(WebServer server, FullHttpRequest request, ChannelHandlerContext ctx) {
        super(server, request.uri());
        this.request = request;
        this.ctx = ctx;
        dataFactory.putData(CTX_KEY, ctx);
    }

    @Override
    public String getPath() {
        if (uri == null) {
            uri = new QueryStringDecoder(request.uri());
        }
        return uri.path();
    }

    @Override
    public String getSimplifiedPath() {
        if (prettyPath == null) {
            prettyPath = super.getSimplifiedPath();
        }
        return prettyPath;
    }

    @Override
    public Method getMethod() {
        if (method == null) {
            method = Method.create(request.method().name());
        }
        return method;
    }

    @Override
    public String getIpAddress() {
        if (address == null) {
            address = NettyUtils.transformIpAddress(ctx.channel().remoteAddress().toString());
        }
        return address;
    }

    @Override
    public byte[] getBody() {
        if (content == null) {
            content = request.content().array();
        }
        return content;
    }

    @Override
    public boolean isKeepAlive() {
        if (keepAlive.isMaybe()) {
            keepAlive = AlmostBoolean.fromBoolean(HttpUtil.isKeepAlive(request));
        }
        return keepAlive.decide(true);
    }

    @Override
    public HashMap<String, String> getHeaders() {
        if (headers == null) {
            headers = new HashMap<>();
            for (String name : request.headers().names()) {
                headers.put(name, request.headers().get(name));
            }
        }
        return headers;
    }

    @Override
    public Map<String, ? extends Collection<String>> getGet() {
        if (uri == null) {
            uri = new QueryStringDecoder(request.uri());
        }
        return uri.parameters();
    }

    @Override
    public Map<String, ? extends Collection<String>> getPost() {
        return post;
    }

    @Override
    public Map<String, String> getCookies() {
        if (cookies == null) {
            cookies = new HashMap<>();
            Set<Cookie> cookieSet = ServerCookieDecoder.LAX.decode(request.headers().get("Cookie"));
            cookieSet.forEach(cookie -> cookies.put(cookie.name(), cookie.value()));
        }
        return cookies;
    }

    @Override
    public void setPost(Map<String, ? extends Collection<String>> post) {
        this.post = post;
    }
}
