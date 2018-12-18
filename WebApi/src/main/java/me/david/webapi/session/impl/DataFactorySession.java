package me.david.webapi.session.impl;

import me.david.davidlib.datafactory.DataFactory;
import me.david.webapi.session.Session;

public class DataFactorySession implements Session {

    protected DataFactory dataFactory;

    @Override
    public void onInit() {
        dataFactory = new DataFactory();
    }

    @Override public void onDestruction() {}

}
