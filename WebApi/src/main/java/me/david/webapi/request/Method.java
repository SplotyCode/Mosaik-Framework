package me.david.webapi.request;

import lombok.Getter;

@Getter
public class Method {

    private String method;
    private boolean standard;
    private StandardMethod standardMethod;

    public Method(String raw) {
        method = raw;
        try {
            standardMethod = StandardMethod.valueOf(raw.toUpperCase());
            standard = true;
        } catch (IllegalArgumentException ex) {
            standardMethod = null;
            standard = false;
        }
    }

    public enum StandardMethod {

        GET,
        POST

    }

}
