package io.github.splotycode.mosaik.webapi.handler;

import io.github.splotycode.mosaik.webapi.server.WebServer;

import java.util.Collection;

public interface HandlerFinder {

    WebServer getWebServer();

    Collection<? extends HttpHandler> search();

}
