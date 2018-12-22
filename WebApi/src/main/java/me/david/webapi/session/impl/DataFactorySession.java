package me.david.webapi.session.impl;

import me.david.davidlib.datafactory.DataFactory;
import me.david.webapi.request.Request;
import me.david.webapi.session.Session;

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
