package me.david.webapi.server;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.david.webapi.response.Response;

import java.util.*;

@Getter
@EqualsAndHashCode
public class Request {

    private String path;
    private String ipAddress;
    private Method method;
    private HashMap<String, String> headers = new HashMap<>();
    @Setter private Map<String, ? extends Collection<String>> get;
    private HashMap<String, String> post = new HashMap<>();
    private boolean keepAlive;

    private Response response = new Response(null);

    public Request(String path, String ipAddress, Method method, boolean keepAlive) {
        this.path = path;
        this.ipAddress = ipAddress;
        this.method = method;
        this.keepAlive = keepAlive;
    }

    public Collection<String> getGetParameter(String name) {
        return get.get(name);
    }

    public String getFirstGetParameter(String name) {
        return getGetParameter(name).iterator().next();
    }

    public boolean isGet() {
        return method.isStandard() && method.getStandardMethod() == Method.StandardMethod.GET;
    }

    public boolean isPost() {
        return method.isStandard() && method.getStandardMethod() == Method.StandardMethod.POST;
    }

}
