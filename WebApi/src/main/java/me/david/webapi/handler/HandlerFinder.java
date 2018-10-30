package me.david.webapi.handler;

import java.util.Collection;

public interface HandlerFinder {

    Collection<HttpHandler> search();

}
