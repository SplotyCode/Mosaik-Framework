package io.github.splotycode.mosaik.webapi.handler;

import io.github.splotycode.mosaik.webapi.request.HandleRequestException;
import io.github.splotycode.mosaik.webapi.request.Request;

public interface HttpHandler {

    default String displayName() {
        return getClass().getSimpleName();
    }

    boolean valid(Request request) throws HandleRequestException;

    /**s
     * @param request the request to handle
     * @return should be cancel all oder handlers
     * @throws HandleRequestException error while handling the exception
     */
    boolean handle(Request request) throws HandleRequestException;

    default int priority() {
        return 0;
    }

}
