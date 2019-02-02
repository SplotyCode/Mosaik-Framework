package io.github.splotycode.mosaik.webapi.handler;

import io.github.splotycode.mosaik.util.reflection.classregister.ListClassRegister;

import java.util.Collection;
import java.util.HashSet;

public class StaticHandlerFinder extends ListClassRegister<HttpHandler> implements HandlerFinder {

    public StaticHandlerFinder() {
        super(new HashSet<>());
    }

    @Override
    public Collection<HttpHandler> search() {
        return getList();
    }

}
