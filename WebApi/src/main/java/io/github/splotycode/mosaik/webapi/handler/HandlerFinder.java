package io.github.splotycode.mosaik.webapi.handler;

import java.util.Collection;

public interface HandlerFinder {

    Collection<HttpHandler> search();

}
