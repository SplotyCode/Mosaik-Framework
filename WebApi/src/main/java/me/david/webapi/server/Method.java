package me.david.webapi.server;

import lombok.Getter;

@Getter
public class Method {

    private String method;
    private boolean standart;
    private StandardMethod standardMethod;

    public enum StandardMethod {

        GET,
        POST

    }

}
