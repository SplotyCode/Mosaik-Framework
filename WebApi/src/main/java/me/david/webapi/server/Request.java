package me.david.webapi.server;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.david.webapi.response.Response;

import java.util.HashMap;

@Getter
@EqualsAndHashCode
public class Request {

    private String path;
    private String ipAddress;
    private Method method;
    private HashMap<String, String> headers = new HashMap<>();
    private HashMap<String, String> get = new HashMap<>();
    private HashMap<String, String> post = new HashMap<>();
    private boolean keepAlive;


    private Response response = new Response(null);

    public Request(String path, String ipAddress, Method method, boolean keepAlive) {
        this.path = path;
        this.ipAddress = ipAddress;
        this.method = method;
        this.keepAlive = keepAlive;
    }

    public boolean isGet() {
        return method.isStandard() && method.getStandardMethod() == Method.StandardMethod.GET;
    }

    public boolean isPost() {
        return method.isStandard() && method.getStandardMethod() == Method.StandardMethod.POST;
    }

}
