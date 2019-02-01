package io.github.splotycode.mosaik.webapi.session.impl;

import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.webapi.request.Request;

public class DataFactorySession extends LocalSession {

    protected DataFactory dataFactory;

    @Override
    public void onInit(Request request) {
        super.onInit(request);
        dataFactory = new DataFactory();
    }

    @Override
    public void onDestruction() {

    }

}
