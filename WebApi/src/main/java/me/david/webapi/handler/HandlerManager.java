package me.david.webapi.handler;

import lombok.Getter;
import me.david.davidlib.utils.init.InitialisedOnce;
import me.david.webapi.handler.anotation.AnnotationHandlerFinder;
import me.david.webapi.handler.anotation.parameter.ParameterResolver;
import me.david.webapi.response.Response;
import me.david.webapi.request.HandleRequestException;
import me.david.webapi.request.Request;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Deprecated
public class HandlerManager extends InitialisedOnce {

    private List<HttpHandler> allHandlers = new ArrayList<>();

    @Getter private List<ParameterResolver> globalParameterResolver = new ArrayList<>();

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
        addFinder(new StaticHandlerFinder());
        addFinder(new AnnotationHandlerFinder(null));
    }
}
