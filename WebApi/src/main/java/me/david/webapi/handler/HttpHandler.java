package me.david.webapi.handler;

import me.david.webapi.server.HandleRequestException;
import me.david.webapi.request.Request;

public interface HttpHandler {

    boolean valid(Request request) throws HandleRequestException;

    boolean handle(Request request) throws HandleRequestException;

    default int priority() {
        return 0;
    }

}
