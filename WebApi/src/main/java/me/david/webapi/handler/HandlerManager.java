package me.david.webapi.handler;

import lombok.Getter;
import me.david.davidlib.utils.init.InitialisedOnce;
import me.david.webapi.WebApplication;
import me.david.webapi.handler.anotation.AnnotationHandlerFinder;
import me.david.webapi.handler.anotation.transform.Transformer;
import me.david.webapi.response.Response;
import me.david.webapi.server.HandleRequestException;
import me.david.webapi.server.Request;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HandlerManager extends InitialisedOnce {

    private List<HttpHandler> allHandlers = new ArrayList<>();

    @Getter private List<Transformer> globalTransformer = new ArrayList<>();

    public void addFinder(HandlerFinder finder) {
        if (initialised) throw new IllegalStateException("Can not add finder if Manager is already initialised");
        allHandlers.addAll(finder.search());
    }

    public Response handleRequest(Request request) throws HandleRequestException {
        List<HttpHandler> handlers = allHandlers.stream().filter(handler -> handler.valid(request)).sorted(Comparator.comparingInt(HttpHandler::priority)).collect(Collectors.toList());
        for (HttpHandler handler : handlers) {
            if (handler.handle(request))
                break;
        }
        return request.getResponse();
    }

    @Override
    protected void init() {
        addFinder(StaticHandlerFinder.getInstance());
        addFinder(new AnnotationHandlerFinder(this));
    }
}
