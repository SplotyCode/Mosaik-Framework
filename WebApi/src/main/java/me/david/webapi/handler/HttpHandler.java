package me.david.webapi.handler;

import me.david.webapi.server.Request;

public interface HttpHandler {

    boolean valid(Request request);

    boolean handle(Request request);

    default int priority() {
        return 0;
    }

}
