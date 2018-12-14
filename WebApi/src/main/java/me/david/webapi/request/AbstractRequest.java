package me.david.webapi.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.david.davidlib.utils.EnumUtil;
import me.david.webapi.request.body.RequestContent;
import me.david.webapi.response.Response;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Getter
@EqualsAndHashCode
public class AbstractRequest implements Request {

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


    private Response response = new Response(null);

    public AbstractRequest(String path, String ipAddress, Method method, boolean keepAlive, byte[] body) {
        this.path = path;
        this.ipAddress = ipAddress;
        this.method = method;
        this.keepAlive = keepAlive;
        this.body = body;
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public String getHeader(RequestHeaders header) {
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

}
