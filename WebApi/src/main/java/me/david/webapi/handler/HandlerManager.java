package me.david.webapi.handler;

import me.david.davidlib.utils.init.InitialisedOnce;
import me.david.webapi.handler.anotation.AnnotationHandlerFinder;
import me.david.webapi.server.Request;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HandlerManager extends InitialisedOnce {

    private List<HttpHandler> allHandlers = new ArrayList<>();

    public void addFinder(HandlerFinder finder) {
        if (initialised) throw new IllegalStateException("Can not add finder if Manager is already initialised");
        allHandlers.addAll(finder.search());
    }

    public void handleRequest(Request request) {
        List<HttpHandler> handlers = allHandlers.stream().filter(handler -> handler.valid(request)).sorted(Comparator.comparingInt(HttpHandler::priority)).collect(Collectors.toList());

        for (HttpHandler handler : handlers) {
            if (handler.handle(request))
                break;
        }
    }

    @Override
    protected void init() {
        addFinder(StaticHandlerFinder.getInstance());
        addFinder(new AnnotationHandlerFinder());
    }
}
