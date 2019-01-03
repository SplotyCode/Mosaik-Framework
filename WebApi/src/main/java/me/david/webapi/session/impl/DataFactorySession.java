package me.david.webapi.session.impl;

import me.david.davidlib.util.datafactory.DataFactory;
import me.david.webapi.request.Request;

public class DataFactorySession extends AbstractSession {

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
