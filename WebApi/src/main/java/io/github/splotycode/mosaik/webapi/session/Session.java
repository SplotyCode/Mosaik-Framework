package io.github.splotycode.mosaik.webapi.session;

import io.github.splotycode.mosaik.webapi.request.Request;

public interface Session {

    void onInit(Request request);
    void onDestruction();
    void onRefresh(Request request);

    long lastRefresh();
    String startedIpAddress();

    default boolean hasPermission(String permission) {
        return hasPermission(null, permission);
    }

    default boolean hasPermission(Request request, String permission) {
        return false;
    }

}
