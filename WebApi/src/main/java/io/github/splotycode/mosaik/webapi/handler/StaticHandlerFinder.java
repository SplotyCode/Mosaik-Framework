package io.github.splotycode.mosaik.webapi.handler;

import io.github.splotycode.mosaik.util.reflection.classregister.ListClassRegister;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class StaticHandlerFinder extends ListClassRegister<HttpHandler> implements HandlerFinder {

    private Set<HttpHandler> handlers = new HashSet<>();

    public StaticHandlerFinder() {
        setCollection(handlers);
    }

    @Override
    public Collection<HttpHandler> search() {
        return handlers;
    }

}