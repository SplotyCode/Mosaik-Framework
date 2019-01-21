package io.github.splotycode.mosaik.webapi.request;

import lombok.Getter;

import java.util.HashMap;

@Getter
public class Method {

    private static final HashMap<String, Method> PREPARED_METHODS = new HashMap<>();

    static {
        for (StandardMethod method : StandardMethod.values()) {
            PREPARED_METHODS.put(method.name().toUpperCase(), new Method(method));
        }
    }

    private String method;
    private boolean standard;
    private StandardMethod standardMethod;

    private Method(StandardMethod standardMethod) {
        this.method = standardMethod.name().toUpperCase();
        standard = true;
        this.standardMethod = standardMethod;
    }

    private Method(String raw) {
        method = raw;
        standardMethod = null;
        standard = false;
    }

    public static Method create(String method) {
        Method prepare = PREPARED_METHODS.get(method.toUpperCase());
        if (prepare == null) {
            return new Method(method);
        }
        return prepare;
    }


    public enum StandardMethod {

        GET,
        POST

    }

}
