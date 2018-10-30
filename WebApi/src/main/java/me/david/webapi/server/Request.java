package me.david.webapi.server;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.HashMap;

@Getter
@EqualsAndHashCode
public class Request {

    private String path;
    private String ipAddress;
    private Method method;
    private HashMap<String, String> headers = new HashMap<>();
    private String httpVersion;

    public boolean isGet() {
        return method.isStandart() && method.getStandardMethod() == Method.StandardMethod.GET;
    }

    public boolean isPost() {
        return method.isStandart() && method.getStandardMethod() == Method.StandardMethod.POST;
    }

}
