package me.david.webapi.session.impl;

import me.david.webapi.request.Request;
import me.david.webapi.session.Session;

public abstract class AbstractSession implements Session {

    protected long lastRefresh;
    protected String lastIpAddress, startedIpAddress;

    @Override
    public void onInit(Request request) {
        startedIpAddress = lastIpAddress = request.getIpAddress();
        lastRefresh = System.currentTimeMillis();
    }

    @Override
    public void onRefresh(Request request) {
        lastIpAddress = request.getIpAddress();
        lastRefresh = System.currentTimeMillis();
    }

    @Override
    public long lastRefresh() {
        return lastRefresh;
    }

    @Override
    public String startedIpAddress() {
        return startedIpAddress;
    }

}
