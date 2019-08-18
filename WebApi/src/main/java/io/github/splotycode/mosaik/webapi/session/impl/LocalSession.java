package io.github.splotycode.mosaik.webapi.session.impl;

import io.github.splotycode.mosaik.webapi.request.Request;
import io.github.splotycode.mosaik.webapi.session.Session;
import io.github.splotycode.mosaik.webapi.session.SessionCreator;

public class LocalSession implements Session {

    public static final SessionCreator CREATOR = request -> new LocalSession();

    protected long lastRefresh;
    protected String lastIpAddress, startedIpAddress;

    @Override
    public void onInit(Request request) {
        startedIpAddress = lastIpAddress = request.getIpAddress();
        lastRefresh = System.currentTimeMillis();
    }

    @Override public void onDestruction() {}

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
