package me.david.webapi.session;

import me.david.webapi.request.Request;

public interface Session {

    void onInit();
    void onDestruction();
    void onRefresh(Request request);

    long lastRefresh();

}
