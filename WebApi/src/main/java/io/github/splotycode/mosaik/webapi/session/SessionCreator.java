package io.github.splotycode.mosaik.webapi.session;

import io.github.splotycode.mosaik.webapi.request.Request;

public interface SessionCreator {

    Session createSession(Request request);

}
