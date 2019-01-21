package io.github.splotycode.mosaik.webapi.handler;

import io.github.splotycode.mosaik.webapi.request.HandleRequestException;
import io.github.splotycode.mosaik.webapi.request.Request;

public interface HttpHandler {

    boolean valid(Request request) throws HandleRequestException;

    boolean handle(Request request) throws HandleRequestException;

    default int priority() {
        return 0;
    }

}
