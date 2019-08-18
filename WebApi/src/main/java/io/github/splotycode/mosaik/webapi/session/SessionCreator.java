package io.github.splotycode.mosaik.webapi.session;

import io.github.splotycode.mosaik.webapi.request.Request;

/**
 * This component is not necessarily required.
 * The {@link SessionCreator} creates Sessions.
 */
public interface SessionCreator {

    Session createSession(Request request);

    /**
     * If true it will create sessions when {@link Request#getSession()} gets called and there is no <b><active/b> session.
     */
    default boolean autoCreate() {
        return false;
    }

}
