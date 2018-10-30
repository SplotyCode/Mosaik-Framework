package me.david.webapi.handler;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StaticHandlerFinder implements HandlerFinder {

    @Getter private static StaticHandlerFinder instance = new StaticHandlerFinder();

    private List<HttpHandler> handlers = new ArrayList<>();

    @Override
    public Collection<HttpHandler> search() {
        return handlers;
    }

    public void register(HttpHandler handler) {
        if (!handlers.contains(handler))
            handlers.add(handler);
    }

    public void unregister(HttpHandler handler) {
        if (handlers.contains(handler))
            handlers.remove(handler);
    }

}
