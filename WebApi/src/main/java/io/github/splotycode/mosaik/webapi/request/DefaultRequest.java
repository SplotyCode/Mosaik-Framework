package io.github.splotycode.mosaik.webapi.request;

import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.webapi.request.body.RequestBodyHelper;
import io.github.splotycode.mosaik.webapi.response.CookieKey;
import io.github.splotycode.mosaik.webapi.response.Response;
import io.github.splotycode.mosaik.webapi.server.WebServer;
import io.github.splotycode.mosaik.webapi.session.Session;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import io.github.splotycode.mosaik.util.EnumUtil;
import io.github.splotycode.mosaik.webapi.request.body.RequestContent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Getter
@EqualsAndHashCode
@Deprecated
public class DefaultRequest implements Request {

    private String path;
    private String ipAddress;
    private Method method;
    private HashMap<String, String> headers = new HashMap<>();
    @Setter private Map<String, ? extends Collection<String>> get;
    @Setter private Map<String, ? extends Collection<String>> post;
    @Getter private Map<String, String> cookies = new HashMap<>();
    private boolean keepAlive;
    private RequestContent content;
    private byte[] body;
    private WebServer webServer;
    private String fullUrl;

    @Getter private DataFactory dataFactory;

    private Response response = new Response(null);

    public DefaultRequest(WebServer webServer, String path, String ipAddress, Method method, String fullUrl, boolean keepAlive, byte[] body) {
        this.path = path;
        this.webServer = webServer;
        this.ipAddress = ipAddress;
        this.method = method;
        this.fullUrl = fullUrl;
        this.keepAlive = keepAlive;
        this.body = body;
    }

    public RequestContent getContent() {
        if (content == null) {
            content = RequestBodyHelper.getRequestContent(this, webServer);
        }
        return content;
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public String getHeader(RequestHeader header) {
        return headers.get(EnumUtil.toDisplayName(header));
    }

    public Collection<String> getGetParameter(String name) {
        return get.get(name);
    }

    public Collection<String> getPostParameter(String name) {
        return post.get(name);
    }

    public String getFirstGetParameter(String name) {
        return getGetParameter(name).iterator().next();
    }

    public String getFirstPostParameter(String name) {
        return getPostParameter(name).iterator().next();
    }

    public boolean isGet() {
        return method.isStandard() && method.getStandardMethod() == Method.StandardMethod.GET;
    }

    public boolean isPost() {
        return method.isStandard() && method.getStandardMethod() == Method.StandardMethod.POST;
    }

    @Override
    public String getCookie(String name) {
        return cookies.get(name);
    }

    @Override
    public String getCookie(CookieKey key) {
        return cookies.get(key.getName());
    }

    @Override
    public Session getSession() {
        return null;
    }

}
